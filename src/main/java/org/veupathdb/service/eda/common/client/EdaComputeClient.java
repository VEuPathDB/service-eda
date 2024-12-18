package org.veupathdb.service.eda.common.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vulpine.lib.jcfi.CheckedSupplier;
import org.gusdb.fgputil.DelimitedDataParser;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.veupathdb.lib.jackson.Json;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.compute.EDACompute;
import org.veupathdb.service.eda.compute.jobs.ReservedFiles;
import org.veupathdb.service.eda.compute.plugins.PluginRegistry;
import org.veupathdb.service.eda.generated.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Supplier;

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

  private static final String META_FILE_SEGMENT = "meta";
  private static final String STATS_FILE_SEGMENT = "statistics";

  public static boolean isJobResultsAvailable(String computeName, ComputeRequestBase requestBody) {
    var plugin = PluginRegistry.get(computeName);
    if (plugin == null)
      return false;

    return EDACompute.getOrSubmitComputeJob(plugin, requestBody, false)
      .getStatus()
      .equals(JobStatus.COMPLETE);
  }

  public static ComputedVariableMetadata getJobVariableMetadata(String computeName, ComputeRequestBase requestBody) {
    var either = getResponseFuture(computeName, META_FILE_SEGMENT, requestBody);

    if (either.isEmpty())
      throw new RuntimeException("compute not found");

    try (var stream = either.get().get()) {
      return Json.getMapper().readValue(stream, ComputedVariableMetadata.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static InputStream getJobStatistics(String computeName, ComputeRequestBase requestBody) {
    return getResponseFuture(computeName, STATS_FILE_SEGMENT, requestBody).orElseThrow().get();
  }

  public static <T> T getJobStatistics(String computeName, ComputeRequestBase requestBody, Class<T> expectedStatsClass) {
    var either = getResponseFuture(computeName, STATS_FILE_SEGMENT, requestBody);

    if (either.isEmpty())
      throw new RuntimeException("compute not found");

    try (var stream = either.get().get()) {
      return Json.getMapper().readValue(stream, expectedStatsClass);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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

    try {
      InputStreamReader isReader = new InputStreamReader(tabularDataSupplier.get());
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

  private static Optional<Supplier<InputStream>> getResponseFuture(
    String computeName,
    String fileSegment,
    ComputeRequestBase requestBody
  ) {
    var fileName = switch(fileSegment) {
      case META_FILE_SEGMENT  -> ReservedFiles.OutputMeta;
      case STATS_FILE_SEGMENT -> ReservedFiles.OutputStats;
      default                 -> null;
    };
    if (fileName == null)
      return Optional.empty();


    var plugin = PluginRegistry.get(computeName);
    if (plugin == null)
      return Optional.empty();

    var file = EDACompute.getComputeJobFiles(plugin, requestBody)
      .stream()
      .filter(it -> it.getName().equals(fileName))
      .findFirst()
      .orElse(null);
    if (file == null)
      return Optional.empty();

    return Optional.of(file::open);
  }
}
