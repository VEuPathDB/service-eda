package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
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
    private final StudyAccess _studyAccess;

    public StudyDatasetInfo(String studyId, String datasetId, String sha1Hash, StudyAccess studyAccess) {
      _studyId = studyId;
      _datasetId = datasetId;
      _sha1Hash = sha1Hash;
      _studyAccess = studyAccess;
    }

    public String getStudyId() { return _studyId; }
    public String getDatasetId() { return _datasetId; }
    public String getSha1Hash() { return _sha1Hash; }
    public StudyAccess getStudyAccess() { return _studyAccess; }

    @Override
    public String toString() {
      return new JSONObject()
          .put("studyId", _studyId)
          .put("datasetId", _datasetId)
          .put("sha1Hash", _sha1Hash)
          .put("studyAccess", _studyAccess.toJson())
          .toString();
    }
  }

  public DatasetAccessClient(String baseUrl, Entry<String,String> authHeader) {
    super(baseUrl, authHeader);
  }

  // map from study ID to study info, including permissions for each study
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
        JSONObject studyPerms = dataset.getJSONObject("actionAuthorization");
        StudyAccess studyAccess = new StudyAccess(
          studyPerms.getBoolean("studyMetadata"),
          studyPerms.getBoolean("subsetting"),
          studyPerms.getBoolean("visualizations"),
          studyPerms.getBoolean("resultsFirstPage"),
          studyPerms.getBoolean("resultsAll")
        );
        infoMap.put(studyId, new StudyDatasetInfo(studyId, datasetId, sha1Hash, studyAccess));
      }
      return infoMap;
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to read permissions response", e);
    }
  }

  public StudyAccess getStudyAccess(String studyId) {
    if (!Boolean.parseBoolean(Environment.getOptionalVar(ENABLE_DATASET_ACCESS_RESTRICTIONS, Boolean.TRUE.toString()))) {
      return new StudyAccess(true, true, true, true, true);
    }
    Map<String, StudyDatasetInfo> map = getStudyDatasetInfoMapForUser();
    if (map.containsKey(studyId)) {
      return map.get(studyId).getStudyAccess();
    }
    throw new NotFoundException("Dataset Access: no study found with ID " + studyId + " in [ " + String.join(", ", map.keySet()) + "]");
  }

}
