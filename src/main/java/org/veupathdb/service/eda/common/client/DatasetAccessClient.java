package org.veupathdb.service.eda.common.client;

import org.gusdb.fgputil.runtime.Environment;
import org.json.JSONObject;
import org.veupathdb.service.eda.access.service.permissions.PermissionService;
import org.veupathdb.service.eda.common.auth.StudyAccess;
import org.veupathdb.service.eda.generated.model.DatasetPermissionEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DatasetAccessClient {
  private static final String ENABLE_DATASET_ACCESS_RESTRICTIONS = "ENABLE_DATASET_ACCESS_RESTRICTIONS";

  public static class BasicStudyDatasetInfo {

    private final String _studyId;
    private final String _datasetId;
    private final boolean _isUserStudy;
    private final StudyAccess _studyAccess;

    public BasicStudyDatasetInfo(String datasetId, DatasetPermissionEntry permissions) {
      _datasetId = datasetId;
      _studyId = permissions.getStudyId();
      _isUserStudy = permissions.getIsUserStudy();

      var auth = permissions.getActionAuthorization();

      _studyAccess = new StudyAccess(
        auth.getStudyMetadata(),
        auth.getSubsetting(),
        auth.getVisualizations(),
        auth.getResultsFirstPage(),
        auth.getResultsAll()
      );
    }

    public String getStudyId() { return _studyId; }
    public String getDatasetId() { return _datasetId; }
    public boolean isUserStudy() { return _isUserStudy; }
    public StudyAccess getStudyAccess() { return _studyAccess; }

    @Override
    public String toString() {
      return toJson().toString();
    }

    public JSONObject toJson() {
      return new JSONObject()
          .put("studyId", _studyId)
          .put("datasetId", _datasetId)
          .put("isUserStudy", _isUserStudy)
          .put("studyAccess", _studyAccess.toJson());
    }
  }

  public static class StudyDatasetInfo extends BasicStudyDatasetInfo {

    private final String _sha1Hash;
    private final String _displayName;
    private final String _shortDisplayName;
    private final String _description;

    public StudyDatasetInfo(String datasetId, DatasetPermissionEntry permissions) {
      super(datasetId, permissions);

      _sha1Hash = permissions.getSha1Hash();
      _displayName = permissions.getDisplayName();
      _shortDisplayName = permissions.getShortDisplayName();
      _description = permissions.getDescription();
    }

    public String getSha1Hash() { return _sha1Hash; }
    public String getDisplayName() { return _displayName; }
    public String getShortDisplayName() { return _shortDisplayName; }
    public String getDescription() { return _description; }

    public JSONObject toJson() {
      return super.toJson()
          .put("sha1Hash", _sha1Hash)
          .put("displayName", _displayName)
          .put("shortDisplayName", _shortDisplayName)
          .put("description", _description);
    }
  }

  /**
   * Builds a map from study ID to study info, including permissions for each curated
   * study, plus user studies this user has access to.  Requires a request to the dataset access service.
   *
   * @return study map with study IDs as keys
   */
  public static Map<String, StudyDatasetInfo> getStudyDatasetInfoMapForUser(long userId) {
    try {
      var datasetMap = PermissionService.getUserPermissions(userId);
      var infoMap    = new HashMap<String, StudyDatasetInfo>(datasetMap.size());

      for (var entry : datasetMap.entrySet()) {
        // reorganizing here; response JSON is keyed by dataset ID, but resulting map is keyed by study ID
        infoMap.put(entry.getValue().getStudyId(), new StudyDatasetInfo(entry.getKey(), entry.getValue()));
      }

      return infoMap;
    } catch (Exception e) {
      throw new RuntimeException("Unable to read permissions response", e);
    }
  }

  /**
   * Looks up the permissions for the target user, finds the study for the
   * passed study ID, and returns only the {@link StudyAccess} portion of the
   * dataset found.  This calls {@link #getStudyDatasetInfoMapForUser(long)} so will
   * return an empty {@code Optional} unless the study is a curated study or the
   * user has access via shared user dataset (even if the study exists as a user
   * study this user does not have permissions on).
   * <p>
   * Note this method (but not others) respects the
   * {@link #ENABLE_DATASET_ACCESS_RESTRICTIONS} environment variable; if set to
   * {@code false}, the dataset access service is NOT queried, and a
   * {@link StudyAccess} object is returned granting universal access to the
   * study.
   * <p>
   * This was a hack added during development to support DBs not entirely
   * populated with data and should eventually be removed.
   *
   * @param studyId study for which perms should be looked up
   *
   * @param userId Target user whose permissions should be checked.
   *
   * @return set of access permissions to this study
   */
  public static Optional<StudyAccess> getStudyAccessByStudyId(String studyId, long userId) {
    if (!Boolean.parseBoolean(Environment.getOptionalVar(ENABLE_DATASET_ACCESS_RESTRICTIONS, Boolean.TRUE.toString()))) {
      return Optional.of(new StudyAccess(true, true, true, true, true));
    }

    // get the perms for this user of known studies
    return Optional.ofNullable(getStudyDatasetInfoMapForUser(userId).get(studyId))
      // fish out the perms
      .map(BasicStudyDatasetInfo::getStudyAccess);
  }
}
