package org.veupathdb.service.eda.common.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.service.eda.common.client.EdaSubsettingClient;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Un-cached resolution of a {@link StudyId} (plain or union) into either the raw
 * {@link APIStudyDetail} or a {@link ReferenceMetadata} instance. A plain id is a pure
 * pass-through to the supplied fetcher; a union id fetches each member and merges them via
 * {@link MetaStudyMerger}.
 * <p>
 * This is Tier 1 only: every call performs the full fetch+merge work. A later task wraps this
 * with a cache.
 */
public final class MetaStudyResolver {

  private static final Logger LOG = LogManager.getLogger(MetaStudyResolver.class);

  private MetaStudyResolver() {}

  // shared across requests; keyed by union token + member data-versions, so it self-invalidates
  // whenever a member's data-version changes. Single-study requests never touch this.
  private static final MetaStudyCache CACHE = new MetaStudyCache();

  public static APIStudyDetail resolveStudyDetail(StudyId id, Function<String, APIStudyDetail> memberFetcher) {
    if (!id.isUnion()) {
      return memberFetcher.apply(id.getMembers().get(0));
    }
    List<APIStudyDetail> members = id.getMembers().stream().map(memberFetcher).toList();
    return MetaStudyMerger.merge(id, members);
  }

  public static ReferenceMetadata resolveReferenceMetadata(StudyId id, Function<String, APIStudyDetail> memberFetcher) {
    if (!id.isUnion()) {
      return new ReferenceMetadata(memberFetcher.apply(id.getMembers().get(0)));
    }
    APIStudyDetail merged = resolveStudyDetail(id, memberFetcher);
    return new ReferenceMetadata(merged, id.getMembers());
  }

  /**
   * Cached resolution of a {@link StudyId} into an {@link APIStudyDetail}. A plain id is a pure
   * pass-through to {@code memberFetcher} (never touches the cache). A union id is looked up in a
   * shared static {@link MetaStudyCache}, keyed by the union token plus every member's
   * data-version (as reported by {@code versionOf}), so a merge is (re)built only when the token
   * is first seen or when a member's version advances.
   * <p>
   * NULL-SAFETY: {@code versionOf} may return {@code null} for a member (e.g. a study whose
   * {@code lastModified} is not populated). A null version must never be baked into a cache key
   * that would then be considered stable forever, so if ANY member's version is null, the cache
   * is bypassed entirely and the merge is built fresh via {@link MetaStudyMerger#merge} directly.
   *
   * @param id union or plain study id
   * @param memberFetcher fetches the raw (uncached) {@link APIStudyDetail} for a member id
   * @param versionOf returns a member's data-version (epoch millis), or {@code null} if unknown
   * @return resolved (merged, for unions) study detail
   */
  public static APIStudyDetail resolveStudyDetailCached(
      StudyId id, Function<String, APIStudyDetail> memberFetcher, Function<String, Long> versionOf) {
    if (!id.isUnion()) return memberFetcher.apply(id.getMembers().get(0));
    boolean anyVersionUnknown = id.getMembers().stream().anyMatch(member -> versionOf.apply(member) == null);
    if (anyVersionUnknown) {
      // can't form a safe cache key; rebuild fresh every time rather than caching under a "null" key.
      // Tier 2 still runs here (via buildAndVerify) -- only caching is skipped, not the value check.
      return buildAndVerify(id, memberFetcher);
    }
    return CACHE.getOrBuild(id, versionOf, () -> buildAndVerify(id, memberFetcher));
  }

  /**
   * Tier 1 (merge) + Tier 2 (deep value-consistency check) for a union, run once per data-version
   * because the caller (a {@link MetaStudyCache} builder supplier) is only invoked on a cache miss.
   * A Tier-2 conflict throws {@link jakarta.ws.rs.BadRequestException}, which propagates so the
   * cache stores the known-invalid verdict rather than silently returning a bad merge.
   */
  private static APIStudyDetail buildAndVerify(StudyId id, Function<String, APIStudyDetail> memberFetcher) {
    APIStudyDetail merged = MetaStudyMerger.merge(id, id.getMembers().stream().map(memberFetcher).toList());
    ReferenceMetadata md = new ReferenceMetadata(merged, id.getMembers());
    EntityDef root = md.getEntity(merged.getRootEntity().getId()).orElseThrow();

    long start = System.currentTimeMillis();
    long[] rowCount = { 0 };
    UnionValueConsistencyChecker.check(md, root, (studyId, entityId) -> {
      var delegate = EdaSubsettingClient.getTabularDataIterator(studyId, List.of(), allNativeVarsStreamSpec(md, entityId));
      return new org.gusdb.fgputil.iterator.CloseableIterator<java.util.Map<String, String>>() {
        @Override public void close() throws Exception { delegate.close(); }
        @Override public boolean hasNext() { return delegate.hasNext(); }
        @Override public java.util.Map<String, String> next() { rowCount[0]++; return delegate.next(); }
      };
    });
    LOG.info("Tier-2 union value-consistency check for '{}' passed: {} rows scanned in {} ms",
      id.getRawToken(), rowCount[0], System.currentTimeMillis() - start);

    return merged;
  }

  /**
   * Builds a {@link StreamSpec} containing all of the given entity's NATIVE variables (i.e.
   * excluding ID, inherited, computed, and derived vars), for use as the Tier-2 checker's data
   * request against a single union member.
   */
  private static StreamSpec allNativeVarsStreamSpec(ReferenceMetadata md, String entityId) {
    EntityDef entity = md.getEntity(entityId).orElseThrow(() ->
      new IllegalStateException("Entity '" + entityId + "' not found in union metadata"));
    StreamSpec spec = new StreamSpec("tier2-" + UUID.randomUUID(), entityId);
    entity.getVariables().stream()
      .filter(v -> v.getSource() == VariableSource.NATIVE)
      .forEach(spec::addVar);
    return spec;
  }
}
