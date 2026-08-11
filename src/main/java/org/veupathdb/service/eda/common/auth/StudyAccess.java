package org.veupathdb.service.eda.common.auth;

import jakarta.ws.rs.ForbiddenException;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.client.DatasetAccessClient;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents a set of permissions a single user has for a single study.  Also provides
 * a static convenience method that can fetch these permissions from a running dataset
 * access service and test them against a predicate.
 */
public class StudyAccess {

  private final boolean _allowStudyMetadata;
  private final boolean _allowSubsetting;
  private final boolean _allowVisualizations;
  private final boolean _allowResultsFirstPage;
  private final boolean _allowResultsAll;

  public StudyAccess(
      final boolean allowStudyMetadata,
      final boolean allowSubsetting,
      final boolean allowVisualizations,
      final boolean allowResultsFirstPage,
      final boolean allowResultsAll
  ) {
    _allowStudyMetadata = allowStudyMetadata;
    _allowSubsetting = allowSubsetting;
    _allowVisualizations = allowVisualizations;
    _allowResultsFirstPage = allowResultsFirstPage;
    _allowResultsAll = allowResultsAll;
  }

  public boolean allowStudyMetadata() { return _allowStudyMetadata; }
  public boolean allowSubsetting() { return _allowSubsetting; }
  public boolean allowVisualizations() { return _allowVisualizations; }
  public boolean allowResultsFirstPage() { return _allowResultsFirstPage; }
  public boolean allowResultsAll() { return _allowResultsAll; }

  /**
   * Looks up study metadata including whether the study is a user study, and
   * permissions for the user represented by the passed header.
   *
   * @param userId ID of the user whose permissions are being tested.
   *
   * @param studyId ID of the study to look up
   *
   * @param accessPredicate Predicate used to test for access given a
   * permissions map.
   *
   * @throws ForbiddenException if the target user does not have the required
   * permission.
   */
  public static void confirmPermission(long userId, String studyId, Predicate<StudyAccess> accessPredicate) {
    confirmPermission(userId, studyId, accessPredicate, DEFAULT_RESOLVER);
  }

  /** Test/composition seam: resolves a single member studyId to its StudyAccess. */
  @FunctionalInterface
  public interface PerStudyAccessResolver {
    Optional<StudyAccess> resolve(String studyId, long userId);
  }

  // The production resolver: existing single-study lookup.
  private static final PerStudyAccessResolver DEFAULT_RESOLVER =
    DatasetAccessClient::getStudyAccessByStudyId;

  /**
   * Package-visible for testing. Resolves the given studyId token (plain id or
   * union(...) token) via {@link org.veupathdb.service.eda.common.model.StudyId#parse}
   * and requires that EVERY member study satisfy the given predicate. A plain id
   * always resolves to exactly one member (itself), so its behavior is unchanged.
   * The literal union token is never itself authorized as a single study.
   *
   * @throws ForbiddenException if any member fails to resolve or fails the predicate.
   */
  static void confirmPermission(long userId, String studyId,
                                Predicate<StudyAccess> accessPredicate,
                                PerStudyAccessResolver resolver) {
    org.veupathdb.service.eda.common.model.StudyId parsed =
      org.veupathdb.service.eda.common.model.StudyId.parse(studyId);
    for (String member : parsed.getMembers()) {
      resolver.resolve(member, userId)
        .filter(accessPredicate)
        .orElseThrow(ForbiddenException::new);
    }
  }

  /** Test-only factory: an all-true StudyAccess (full access granted). */
  static StudyAccess grantAll() {
    return new StudyAccess(true, true, true, true, true);
  }

  /** Test-only factory: an all-false StudyAccess (no access granted). */
  static StudyAccess denyAll() {
    return new StudyAccess(false, false, false, false, false);
  }

  public JSONObject toJson() {
    return new JSONObject()
      .put("allowStudyMetadata", _allowStudyMetadata)
      .put("allowSubsetting", _allowSubsetting)
      .put("allowVisualizations", _allowVisualizations)
      .put("allowResultsFirstPage", _allowResultsFirstPage)
      .put("allowResultsAll", _allowResultsAll);
  }
}
