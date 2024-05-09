package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.core.MediaType;
import org.gusdb.fgputil.DelimitedDataParser;
import org.gusdb.fgputil.IoUtil;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.gusdb.fgputil.json.JsonUtil;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.merge.core.request.ComputeInfo;
import org.veupathdb.service.eda.subset.model.Entity;
import org.veupathdb.service.eda.subset.model.Study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.gusdb.fgputil.FormatUtil.TAB;

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

  // parent path returns status; flag indicates not to restart
  private static final String STATUS_SEGMENT = "?autostart=false";

  private static final String META_FILE_SEGMENT = "/meta";
  private static final String STATS_FILE_SEGMENT = "/statistics";
  private static final String TABULAR_FILE_SEGMENT = "/tabular";

  private final String _baseComputesUrl;
  private final Map<String, String> _authHeader;

  public EdaComputeClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    _baseComputesUrl = serviceBaseUrl + "/computes/";
    _authHeader = Map.of(authHeader.getKey(), authHeader.getValue());
  }

  public boolean isJobResultsAvailable(String computeName, ComputeRequestBody requestBody) {
    JobResponse response = readJsonResponse(getResponseFuture(computeName, STATUS_SEGMENT, requestBody), JobResponse.class);
    return response.getStatus().equals(JobStatus.COMPLETE);
  }

  public ComputedVariableMetadata getJobVariableMetadata(String computeName, ComputeRequestBody requestBody) {
    return readJsonResponse(getResponseFuture(computeName, META_FILE_SEGMENT, requestBody), ComputedVariableMetadataImpl.class);
  }

  public ResponseFuture getJobStatistics(String computeName, ComputeRequestBody requestBody) {
    return getResponseFuture(computeName, STATS_FILE_SEGMENT, requestBody);
  }

  public <T> T getJobStatistics(String computeName, ComputeRequestBody requestBody, Class<T> expectedStatsClass) {
    return readJsonResponse(getResponseFuture(computeName, STATS_FILE_SEGMENT, requestBody), expectedStatsClass);
  }

  /**
   * TODO migrate this to directly use compute functionality:
   *  org.veupathdb.service.eda.compute.controller.ComputeController#resultFile(
   *  org.veupathdb.service.eda.compute.plugins.PluginMeta, java.lang.String,
   *  org.veupathdb.service.eda.generated.model.ComputeRequestBase, java.util.function.Function)
   *
   *  This isn't expected to provide a particularly noticable performance improvement, but it will avoid overhead
   *  of HTTP.
   */
  public CloseableIterator<Map<String, String>> getJobTabularIteratorOutput(List<EntityDef> ancestors,
                                                                            String computeName,
                                                                            EntityDef computeEntity,
                                                                            List<VariableMapping> variables,
                                                                            ComputeRequestBody requestBody) {
    // Construct expected headers from metadata. These are used for validation of the incoming compute stream.
    List<String> expectedHeaders = new ArrayList<>();
    expectedHeaders.add(computeEntity.getIdColumnDef().getVariableId());
    ancestors.forEach(anc -> expectedHeaders.add(anc.getIdColumnDef().getVariableId()));
    variables.forEach(var -> expectedHeaders.add(var.getVariableSpec().getVariableId()));

    // Output rows should have dot-notation, so we construct the data parser with these headers.
    List<String> outputHeaders = new ArrayList<>();
    outputHeaders.add(toDotNotation(computeEntity.getIdColumnDef()));
    ancestors.forEach(anc -> outputHeaders.add(toDotNotation(anc.getIdColumnDef())));
    variables.forEach(var -> outputHeaders.add(toDotNotation(var.getVariableSpec())));

    DelimitedDataParser d = new DelimitedDataParser(outputHeaders, TAB, true);
    try {
      InputStream is = getJobTabularOutput(computeName, requestBody).getInputStream();
      InputStreamReader isReader = new InputStreamReader(is);
      BufferedReader bufferedReader = new BufferedReader(isReader);
      String headerLine = bufferedReader.readLine();

      if (headerLine == null) {
        throw new RuntimeException("Compute stream is empty.");
      }

      List<String> received = List.of(headerLine.split(TAB));

      for (int i = 0; i < received.size(); i++) {
        if (!received.get(i).equals(expectedHeaders.get(i))) { // validates header names

          throw new RuntimeException("Tabular subsetting result of type '" +
              computeEntity.getId() + "' contained unexpected header. Expected:" +
              expectedHeaders + ". Found: " + String.join(",", received));
        }
      }

      // Convert from InputStream to iterator.
      return new CloseableIterator<>() {
        private String nextLine = bufferedReader.readLine();

        @Override
        public void close() throws Exception {
          bufferedReader.close();
        }

        @Override
        public boolean hasNext() {
          return nextLine != null;
        }

        @Override
        public Map<String, String> next() {
          Map<String, String> record = d.parseLine(nextLine);
          try {
            nextLine = bufferedReader.readLine();
          } catch (IOException e) {
            throw new RuntimeException("Failed to read from compute stream buffered reader.", e);
          }
          return record;
        }
      };
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String toDotNotation(VariableDef variableDef) {
    return variableDef.getEntityId() + "." + variableDef.getVariableId();
  }

  private String toDotNotation(VariableSpec variableSpec) {
    return variableSpec.getEntityId() + "." + variableSpec.getVariableId();
  }

  public ResponseFuture getJobTabularOutput(String computeName, ComputeRequestBody requestBody) {
    return getResponseFuture(computeName, TABULAR_FILE_SEGMENT, requestBody);
  }

  private ResponseFuture getResponseFuture(String computeName, String fileSegment, ComputeRequestBody requestBody) {
    return ClientUtil.makeAsyncPostRequest(
        // note: need to use wildcard here since compute service serves all result files out at the same endpoint
        _baseComputesUrl + computeName + fileSegment, requestBody, MediaType.MEDIA_TYPE_WILDCARD, _authHeader);
  }

  private <T> T readJsonResponse(ResponseFuture response, Class<T> responseClass) {
    try (InputStream responseBody = response.getEither().leftOrElseThrowWithRight(f -> new RuntimeException(f.toString()))) {
      String json = IoUtil.readAllChars(new InputStreamReader(responseBody));
      return JsonUtil.Jackson.readValue(json, responseClass);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to make compute request or read/convert response", e);
    }
  }
}
