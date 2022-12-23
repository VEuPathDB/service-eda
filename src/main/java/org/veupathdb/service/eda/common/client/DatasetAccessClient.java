package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.runtime.Environment;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.auth.StudyAccess;

public class DatasetAccessClient extends ServiceClient {

  private static final String ENABLE_DATASET_ACCESS_RESTRICTIONS = "ENABLE_DATASET_ACCESS_RESTRICTIONS";

  public static class StudyDatasetInfo {

    private final String _studyId;
    private final String _datasetId;
    private final String _sha1Hash;
    private final boolean _isUserStudy;
    private final StudyAccess _studyAccess;
    private final String _displayName;
    private final String _shortDisplayName;
    private final String _description;

    public StudyDatasetInfo(
        String studyId,
        String datasetId,
        String sha1Hash,
        boolean isUserStudy,
        StudyAccess studyAccess,
        String displayName,
        String shortDisplayName,
        String description) {
      _studyId = studyId;
      _datasetId = datasetId;
      _sha1Hash = sha1Hash;
      _isUserStudy = isUserStudy;
      _studyAccess = studyAccess;
      _displayName = displayName;
      _shortDisplayName = shortDisplayName;
      _description = description;
    }

    public String getStudyId() { return _studyId; }
    public String getDatasetId() { return _datasetId; }
    public String getSha1Hash() { return _sha1Hash; }
    public boolean isUserStudy() { return _isUserStudy; }
    public StudyAccess getStudyAccess() { return _studyAccess; }
    public String getDisplayName() { return _displayName; }
    public String getShortDisplayName() { return _shortDisplayName; }
    public String getDescription() { return _description; }

    @Override
    public String toString() {
      return new JSONObject()
          .put("studyId", _studyId)
          .put("datasetId", _datasetId)
          .put("sha1Hash", _sha1Hash)
          .put("isUserStudy", _isUserStudy)
          .put("studyAccess", _studyAccess.toJson())
          .put("displayName", _displayName)
          .put("shortDisplayName", _shortDisplayName)
          .put("description", _description)
          .toString();
    }
  }

  public DatasetAccessClient(String baseUrl, Entry<String,String> authHeader) {
    super(baseUrl, authHeader);
  }

  /**
   * Builds a map from study ID to study info, including permissions for each study;
   * requires a request to the dataset access service.
   *
   * @return study map
   */
  public Map<String, StudyDatasetInfo> getStudyDatasetInfoMapForUser() {
    try (InputStream response = ClientUtil.makeAsyncGetRequest(getUrl("/permissions"),
        MediaType.APPLICATION_JSON, getAuthHeaderMap()).getInputStream()) {
      String permissionsJson = ClientUtil.readSmallResponseBody(response);
      JSONObject datasetMap = new JSONObject(permissionsJson).getJSONObject("perDataset");
      Map<String, StudyDatasetInfo> infoMap = new HashMap<>();
      for (String datasetId : datasetMap.keySet()) {
        JSONObject dataset = datasetMap.getJSONObject(datasetId);
        String studyId = dataset.getString("studyId");
        String sha1Hash = dataset.getString("sha1Hash");
        boolean isUserStudy = dataset.getBoolean("isUserStudy");
        String displayName = dataset.getString("displayName");
        String shortDisplayName = dataset.getString("shortDisplayName");
        String description = dataset.getString("description");
        JSONObject studyPerms = dataset.getJSONObject("actionAuthorization");
        StudyAccess studyAccess = new StudyAccess(
          studyPerms.getBoolean("studyMetadata"),
          studyPerms.getBoolean("subsetting"),
          studyPerms.getBoolean("visualizations"),
          studyPerms.getBoolean("resultsFirstPage"),
          studyPerms.getBoolean("resultsAll")
        );
        infoMap.put(studyId, new StudyDatasetInfo(studyId, datasetId, sha1Hash, isUserStudy, studyAccess, displayName, shortDisplayName, description));
      }
      return infoMap;
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to read permissions response", e);
    }
  }

  /**
   * Calls <code>getStudyDatasetInfoMapForUser()</code> and looks up the passed
   * study ID in the returned map, returning the entry's <code>StudyDatasetInfo</code>
   * if present, throwing a <code>NotFoundException</code> if not.
   *
   * @param studyId study to look up
   * @return dataset access service metadata about this study
   */
  public StudyDatasetInfo getStudyDatasetInfo(String studyId) {
    Map<String, StudyDatasetInfo> map = getStudyDatasetInfoMapForUser();
    if (map.containsKey(studyId)) {
      return map.get(studyId);
    }
    throw new NotFoundException("Dataset Access: no study found with ID " + studyId + " in [ " + String.join(", ", map.keySet()) + "]");
  }

  /**
   * Returns only the StudyAccess portion of the found StudyDatasetInfo.
   *
   * Note this method (but not others) respects the ENABLE_DATASET_ACCESS_RESTRICTIONS environment variable;
   * if set to false, the the dataset access service is NOT queried, and a StudyAccess object is returned
   * granting universal access to the study.  This was a hack added during development to support DBs not
   * entirely populated with data and should eventually be removed.
   *
   * @param studyId study for which perms should be looked up
   * @return set of access permissions to this study
   */
  public StudyAccess getStudyAccess(String studyId) {
    if (!Boolean.parseBoolean(Environment.getOptionalVar(ENABLE_DATASET_ACCESS_RESTRICTIONS, Boolean.TRUE.toString()))) {
      return new StudyAccess(true, true, true, true, true);
    }
    return getStudyDatasetInfo(studyId).getStudyAccess();
  }

}
