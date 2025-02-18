package org.veupathdb.service.eda.data.plugin.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.EncryptionUtil;
import org.gusdb.fgputil.Timer;
import org.gusdb.fgputil.Wrapper;
import org.gusdb.fgputil.cache.ManagedMap;
import org.gusdb.fgputil.cache.ValueProductionException;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.data.core.AbstractEmptyComputePlugin;
import org.veupathdb.service.eda.generated.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RecordCountPlugin extends AbstractEmptyComputePlugin<RecordCountPostRequest, RecordCountSpec> {

  private static final Logger LOG = LogManager.getLogger(RecordCountPlugin.class);

  private static final ManagedMap<String,RecordCountPostResponse> RESULT_CACHE =
      new ManagedMap<>(5000, 2000);

  private String _cacheKey;
  private RecordCountPostResponse _cachedResponse;

  @Override
  public String getDisplayName() {
    return "Record Count";
  }

  @Override
  public String getDescription() {
    return "Counts how many rows in a single stream of records";
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(RecordCountPostRequest.class, RecordCountSpec.class);
  }

  @Override
  protected void validateVisualizationSpec(RecordCountSpec pluginSpec) throws ValidationException {
    getReferenceMetadata().getEntity(pluginSpec.getEntityId())
        .orElseThrow(() -> new ValidationException("Invalid entity ID: " + pluginSpec.getEntityId()));
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(RecordCountSpec pluginSpec) {
    String entityId = pluginSpec.getEntityId();
    _cacheKey = EncryptionUtil.md5(
        JsonUtil.serializeObject(getSubsetFilters()) +
        "|" + getReferenceMetadata().getStudyId() + "|" + entityId
    );
    _cachedResponse = RESULT_CACHE.get(_cacheKey);
    LOG.info("Did I find a cached response? {}", _cachedResponse != null);

    // only need one stream for the requested entity and no vars (IDs included automatically)
    return _cachedResponse != null ? Collections.emptyList() : List.of(
      new StreamSpec(pluginSpec.getEntityId(), pluginSpec.getEntityId())
        // add first var in entity to work around no-vars bug in subsetting service
        .addVar(getReferenceMetadata().getEntity(pluginSpec.getEntityId()).orElseThrow().getVariables().stream()
            .filter(var -> VariableSource.NATIVE.equals(var.getSource()))
            .findFirst().orElseThrow())); // should have at least one native var
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) throws IOException {
    if (_cachedResponse == null) {
      LOG.info("Result not in cache; calculating");
      try {
        _cachedResponse = RESULT_CACHE.getValue(_cacheKey, key -> {
          Timer t = new Timer();
          long subsettingRowCount = getSubsetCount(_pluginSpec.getEntityId());
          LOG.info("Retrieved record count from subsetting ({}) in {}", subsettingRowCount, t.getElapsedStringAndRestart());
          Wrapper<Long> rowCount = new Wrapper<>(0L);
          new Scanner(dataStreams.get(getPluginSpec().getEntityId()))
            .useDelimiter("\n")
            .forEachRemaining(str -> rowCount.set(rowCount.get() + 1));
          long recordCount = rowCount.get() - 1; // subtract 1 for header row
          RecordCountPostResponse value = new RecordCountPostResponseImpl();
          value.setRecordCount((int)recordCount);
          LOG.info("Calculated record count result to add to cache ({}) in {}", recordCount, t.getElapsedString());
          return value;
        });
      }
      catch (ValueProductionException e) {
        throw new RuntimeException("Could not generate entity count", e);
      }
    }
    out.write(JsonUtil.serializeObject(_cachedResponse).getBytes());
    out.flush();
  }
}
