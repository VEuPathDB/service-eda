package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.core.MediaType;
import org.gusdb.fgputil.IoUtil;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.json.JsonUtil;
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EdaComputeClient {

  public static class ComputeRequestBody extends ComputeRequestBaseImpl {

    @JsonIgnore
    private final Object _computeConfig;

    public ComputeRequestBody(
        String studyId, List<APIFilter> subset, List<DerivedVariableSpec> derivedVariables, Object computeConfig) {
      setStudyId(studyId);
      setFilters(subset);
      setDerivedVariables(derivedVariables);
      _computeConfig = computeConfig;
    }

    @JsonProperty("config")
    public Object getConfig() {
      return _computeConfig;
    }
  }

  private final String _baseComputesUrl;
  private final Map<String, String> _authHeader;

  public EdaComputeClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    _baseComputesUrl = serviceBaseUrl + "/computes/";
    _authHeader = Map.of(authHeader.getKey(), authHeader.getValue());
  }

  public boolean isJobResultsAvailable(String computeName, ComputeRequestBody requestBody) {
    JobResponse response = readJsonResponse(computeName + "?autostart=false", requestBody, JobResponse.class);
    return response.getStatus().equals(JobStatus.COMPLETE);
  }

  public ComputedVariableMetadata getJobVariableMetadata(String computeName, ComputeRequestBody requestBody) {
    return readJsonResponse(computeName + "/meta", requestBody, ComputedVariableMetadataImpl.class);
  }

  public <T> T getJobStatistics(String computeName, ComputeRequestBody requestBody, Class<T> expectedStatsClass) {
    return readJsonResponse(computeName + "/statistics", requestBody, expectedStatsClass);
  }

  public ResponseFuture getJobTabularOutput(String computeName, ComputeRequestBody requestBody) {
    return ClientUtil.makeAsyncPostRequest(
        // note: need to use wildcard here since compute service serves all result files out at the same endpoint
        _baseComputesUrl + computeName + "/tabular", requestBody, MediaType.MEDIA_TYPE_WILDCARD, _authHeader);
  }

  private <T> T readJsonResponse(String pathSuffix, ComputeRequestBase computeConfig, Class<T> responseClass) {
    ResponseFuture response = ClientUtil.makeAsyncPostRequest(
        // note: need to use wildcard here since compute service serves all result files out at the same endpoint
        _baseComputesUrl + pathSuffix, computeConfig, MediaType.MEDIA_TYPE_WILDCARD, _authHeader);
    try (InputStream responseBody = response.getEither().leftOrElseThrowWithRight(f -> new RuntimeException(f.toString()))) {
      String json = IoUtil.readAllChars(new InputStreamReader(responseBody));
      return JsonUtil.Jackson.readValue(json, responseClass);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to make compute request or read/convert response", e);
    }
  }
}
