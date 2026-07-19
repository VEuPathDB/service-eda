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
}
