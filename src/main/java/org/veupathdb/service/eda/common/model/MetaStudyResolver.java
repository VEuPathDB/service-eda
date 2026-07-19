package org.veupathdb.service.eda.common.model;

import org.veupathdb.service.eda.generated.model.APIStudyDetail;

import java.util.List;
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
      // can't form a safe cache key; rebuild fresh every time rather than caching under a "null" key
      return MetaStudyMerger.merge(id, id.getMembers().stream().map(memberFetcher).toList());
    }
    return CACHE.getOrBuild(id, versionOf, () -> MetaStudyMerger.merge(id, id.getMembers().stream().map(memberFetcher).toList()));
  }
}
