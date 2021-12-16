package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.Map.Entry;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.runtime.Environment;
import org.json.JSONObject;
import org.veupathdb.service.eda.common.auth.StudyAccess;

public class DatasetAccessClient extends ServiceClient {

  private static final String ENABLE_DATASET_ACCESS_RESTRICTIONS = "ENABLE_DATASET_ACCESS_RESTRICTIONS";

  public DatasetAccessClient(String baseUrl, Entry<String,String> authHeader) {
    super(baseUrl, authHeader);
  }

  public StudyAccess getStudyAccess(String studyId) {
    if (!Boolean.parseBoolean(Environment.getOptionalVar(ENABLE_DATASET_ACCESS_RESTRICTIONS, Boolean.TRUE.toString()))) {
      return new StudyAccess(true, true, true, true, true);
    }
    try (InputStream response = ClientUtil.makeAsyncGetRequest(getUrl("/permissions"),
        MediaType.APPLICATION_JSON, getAuthHeaderMap()).getInputStream()) {
      String permissionsJson = ClientUtil.readSmallResponseBody(response);
      JSONObject datasetMap = new JSONObject(permissionsJson).getJSONObject("perDataset");
      JSONObject studyPerms = findStudyPermsObject(datasetMap, studyId);
      return new StudyAccess(
          studyPerms.getBoolean("studyMetadata"),
          studyPerms.getBoolean("subsetting"),
          studyPerms.getBoolean("visualizations"),
          studyPerms.getBoolean("resultsFirstPage"),
          studyPerms.getBoolean("resultsAll")
      );
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to read permissions response", e);
    }
  }

  private JSONObject findStudyPermsObject(JSONObject datasets, String studyId) {
    for (String datasetId : datasets.keySet()) {
      JSONObject dataset = datasets.getJSONObject(datasetId);
      if (dataset.getString("studyId").equals(studyId)) {
        return dataset.getJSONObject("actionAuthorization");
      }
    }
    throw new NotFoundException("Dataset Access: no study found with ID " + studyId + " in " + datasets);
  }

}
