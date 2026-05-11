package org.veupathdb.service.eda.compute.plugins.rankedabundance;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.CollectionDef;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.RankedAbundanceComputeConfig;
import org.veupathdb.service.eda.generated.model.RankedAbundancePluginRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankedAbundancePlugin extends AbstractPlugin<RankedAbundancePluginRequest, RankedAbundanceComputeConfig> {

  private static final String INPUT_DATA = "ranked_abundance_input";
  private static final String SAMPLE_DATA = "sample_entity_input";

  public RankedAbundancePlugin(@NotNull PluginContext<RankedAbundancePluginRequest, RankedAbundanceComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    String entityId = getConfig().getCollectionVariable().getEntityId();
    return List.of(
      new StreamSpec(INPUT_DATA, entityId)
        .addVars(getUtil().getCollectionMembers(getConfig().getCollectionVariable())),
      new StreamSpec(SAMPLE_DATA, entityId)  // just ID columns, no extra vars
    );
  }

  @Override
  protected void execute() {

    RankedAbundanceComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();
    CollectionDef collection = meta.getCollection(computeConfig.getCollectionVariable()).orElseThrow();
    String collectionMemberType = collection.getMember() == null ? "unknown" : collection.getMember();
    String entityId = computeConfig.getCollectionVariable().getEntityId();
    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String shortIdColName = computeEntityIdColName.contains(".")
      ? computeEntityIdColName.substring(computeEntityIdColName.lastIndexOf('.') + 1)
      : computeEntityIdColName;
    String method = computeConfig.getRankingMethod().getValue();
    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));
    dataStream.put(SAMPLE_DATA, getWorkspace().openStream(SAMPLE_DATA));
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting ranked abundance computation')");

      List<VariableSpec> computeInputVars = new ArrayList<>() {{
        add(computeEntityIdVarSpec);
        addAll(util.getCollectionMembers(computeConfig.getCollectionVariable()));
        addAll(idColumns);
      }};
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
      List<VariableSpec> sampleIdVars = new ArrayList<>();
      sampleIdVars.add(computeEntityIdVarSpec);
      sampleIdVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(SAMPLE_DATA, sampleIdVars));
      // Strip entity prefix from all ID column names to match the short names used by the compute output
      connection.voidEval("data.table::setnames(" + SAMPLE_DATA + ", names(" + SAMPLE_DATA + "), sub('^[^.]+\\\\.', '', names(" + SAMPLE_DATA + ")))");
      // TODO make a helper for this i think
      List<String> dotNotatedIdColumns = idColumns.stream().map(VariableDef::toDotNotation).toList();
      StringBuilder dotNotatedIdColumnsString = new StringBuilder("c(");
      boolean first = true;
      for (String idCol : dotNotatedIdColumns) {
        if (first) {
          first = false;
          dotNotatedIdColumnsString.append(PluginUtil.singleQuote(idCol));
        } else {
          dotNotatedIdColumnsString.append(",").append(PluginUtil.singleQuote(idCol));
        }
      }
      dotNotatedIdColumnsString.append(")");

      connection.voidEval("abundDT <- microbiomeComputations::AbundanceData(name= " + PluginUtil.singleQuote(collectionMemberType) + ",data=" + INPUT_DATA +
                                                                          ",recordIdColumn=" + PluginUtil.singleQuote(computeEntityIdColName) +
                                                                          ",ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                                                          ",imputeZero=TRUE)");
      connection.voidEval("abundanceDT <- rankedAbundance(abundDT, " +
                                                          PluginUtil.singleQuote(method) + ")");

      // Left-join results onto full sample list so samples with no abundance data get NA values.
      connection.voidEval("abundanceData <- abundanceDT@data");
      connection.voidEval("abundanceData <- merge(" + SAMPLE_DATA + ", abundanceData, by=names(" + SAMPLE_DATA + "), all.x=TRUE, sort=FALSE)");
      connection.voidEval("abundanceData <- abundanceData[match(" + SAMPLE_DATA + "[[" +
        PluginUtil.singleQuote(shortIdColName) + "]], abundanceData[[" +
        PluginUtil.singleQuote(shortIdColName) + "]])]");
      connection.voidEval("abundanceDT@data <- abundanceData");

      String dataCmd = "writeData(abundanceDT, NULL, TRUE)";
      String metaCmd = "writeMeta(abundanceDT, NULL, TRUE)";

      getWorkspace().writeDataResult(connection, dataCmd);
      getWorkspace().writeMetaResult(connection, metaCmd);
    });
  }
}
