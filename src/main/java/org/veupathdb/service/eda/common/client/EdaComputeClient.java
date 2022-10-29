package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import org.gusdb.fgputil.IoUtil;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.functional.Functions;
import org.gusdb.fgputil.json.JsonUtil;
import org.json.JSONObject;
import org.veupathdb.service.eda.generated.model.ComputedVariableMetadata;
import org.veupathdb.service.eda.generated.model.ComputedVariableMetadataImpl;
import org.veupathdb.service.eda.generated.model.JobResponse;
import org.veupathdb.service.eda.generated.model.JobStatus;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

public class EdaComputeClient {

  private final String _baseComputesUrl;
  private final Map<String, String> _authHeader;

  public EdaComputeClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    _baseComputesUrl = serviceBaseUrl + "/computes/";
    _authHeader = Map.of(authHeader.getKey(), authHeader.getValue());
  }

  public boolean isResultsAvailable(String computeName, JsonNode computeConfig) {
    JobResponse response = readJsonResponse(computeName, computeConfig, JobResponse.class);
    return response.getStatus().equals(JobStatus.COMPLETE);
  }

  public <T> T getJobStatistics(String computeName, JsonNode computeConfig, Class<T> statsClass) throws Exception {
    return readJsonResponse(computeName + "/statistics", computeConfig, statsClass);
  }

  public ComputedVariableMetadata getVariableMetadata(String computeName, JsonNode computeConfig) {
    return readJsonResponse(computeName + "/meta", computeConfig, ComputedVariableMetadataImpl.class);
  }

  private <T> T readJsonResponse(String pathSuffix, JsonNode computeConfig, Class<T> responseClass) {
    ResponseFuture response = ClientUtil.makeAsyncPostRequest(
        _baseComputesUrl + pathSuffix, computeConfig, MediaType.APPLICATION_JSON, _authHeader);
    try (InputStream responseBody = response.getEither().leftOrElseThrowWithRight(f -> new RuntimeException(f.toString()))) {
      String json = IoUtil.readAllChars(new InputStreamReader(responseBody));
      return JsonUtil.Jackson.readValue(json, responseClass);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to make compute request or read/convert response", e);
    }
  }

  public ResponseFuture getJobTabularOutput(String computeName, JsonNode computeConfig) {
    String url = "/tabular";
    return null;
  }
}
