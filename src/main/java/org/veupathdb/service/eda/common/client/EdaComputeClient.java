package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vulpine.lib.jcfi.CheckedSupplier;
import jakarta.ws.rs.core.MediaType;
import org.gusdb.fgputil.DelimitedDataParser;
import org.gusdb.fgputil.IoUtil;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.gusdb.fgputil.json.JsonUtil;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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


  private static List<VariableSpec> getComputeVars(EntityDef targetEntity, List<VariableMapping> varMappings, ReferenceMetadata metadata) {

    // if no computed vars present, nothing to do
    if (varMappings.isEmpty()) return Collections.emptyList();

    // create variable specs from computed var metadata
    List<VariableSpec> computedVars = new ArrayList<>();

    // first column is the ID col for the returned entity
    computedVars.add(targetEntity.getIdColumnDef());

    // next cols are the ID cols for ancestor entities (up the tree)
    for (EntityDef ancestor : metadata.getAncestors(targetEntity)) {
      computedVars.add(ancestor.getIdColumnDef());
    }

    varMappings.forEach(varMapping -> {
      if (varMapping.getIsCollection()) {
        // for collection vars, expect columns for each member
        computedVars.addAll(varMapping.getMembers());
      }
      else {
        // for non-collections, add the mapping's spec
        computedVars.add(varMapping.getVariableSpec());
      }
    });

    return computedVars;
  }

  /**
   * @deprecated TODO: move me to a more sensible location.
   */
  public static CloseableIterator<Map<String, String>> getJobTabularIteratorOutput(
    EntityDef computeEntity,
    List<VariableMapping> variables,
    ReferenceMetadata referenceMetadata,
    CheckedSupplier<InputStream> tabularDataSupplier
  ) {
    List<String> headers = VariableDef.toDotNotation(getComputeVars(computeEntity, variables, referenceMetadata));

    try(var is = tabularDataSupplier.get()) {
      InputStreamReader isReader = new InputStreamReader(is);
      BufferedReader bufferedReader = new BufferedReader(isReader);
      String headerLine = bufferedReader.readLine();

      if (headerLine == null) {
        throw new RuntimeException("Compute stream is empty.");
      }

      DelimitedDataParser d = new DelimitedDataParser(headers, TAB, true);

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
