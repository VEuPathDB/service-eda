package org.veupathdb.service.eda.compute.plugins.dimensionalityreduction;

import org.gusdb.fgputil.ListBuilder;
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
import org.veupathdb.service.eda.generated.model.DimensionalityReductionComputeConfig;
import org.veupathdb.service.eda.generated.model.DimensionalityReductionPluginRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimensionalityReductionPlugin extends AbstractPlugin<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> {

  private static final String INPUT_DATA = "dimensionality_reduction_input";

  public DimensionalityReductionPlugin(@NotNull PluginContext<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    return List.of(new StreamSpec(INPUT_DATA, getConfig().getCollectionVariable().getEntityId())
      .addVars(getUtil().getCollectionMembers(getConfig().getCollectionVariable())));
  }

  @Override
  protected void execute() {

    DimensionalityReductionComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();
    CollectionDef collection = meta.getCollection(computeConfig.getCollectionVariable()).orElseThrow();
    String collectionMemberType = collection.getMember() == null ? "unknown" : collection.getMember();
    String entityId = computeConfig.getCollectionVariable().getEntityId();
    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String nPCs = computeConfig.getNPCs() == null ? "5" : computeConfig.getNPCs().toString();
    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting dimensionality reduction computation')");

      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getCollectionMembers(computeConfig.getCollectionVariable()));
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
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

      connection.voidEval("abundDT <- microbiomeComputations::AbundanceData(name=" + PluginUtil.singleQuote(collectionMemberType) + ",data=" + INPUT_DATA +
        ",recordIdColumn=" + PluginUtil.singleQuote(computeEntityIdColName) +
        ",ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
        ",imputeZero=TRUE)");

      connection.voidEval("pcaOutput <- veupathUtils::pca(abundDT, " +
        "nPCs=" + PluginUtil.singleQuote(nPCs) + ", " +
        "verbose=TRUE)");
      String dataCmd = "writeData(pcaOutput, NULL, TRUE)";
      String metaCmd = "writeMeta(pcaOutput, NULL, TRUE)";

      getWorkspace().writeDataResult(connection, dataCmd);
      getWorkspace().writeMetaResult(connection, metaCmd);
    });
  }
}
