package org.veupathdb.service.eda.compute.plugins.dimensionalityreduction;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
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

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

public class DimensionalityReductionPlugin extends AbstractPlugin<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> {

  private static final String INPUT_DATA = "dimensionality_reduction_input";

  public DimensionalityReductionPlugin(@NotNull PluginContext<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    VariableSpec identifierVar = getConfig().getIdentifierVariable();
    VariableSpec valueVar = getConfig().getValueVariable();
    return List.of(new StreamSpec(INPUT_DATA, identifierVar.getEntityId())
      .addVar(identifierVar)
      .addVar(valueVar));
  }

  @Override
  protected void execute() {

    DimensionalityReductionComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();

    VariableSpec identifierVarSpec = computeConfig.getIdentifierVariable();
    VariableSpec valueVarSpec = computeConfig.getValueVariable();
    String entityId = identifierVarSpec.getEntityId();
    String collectionMemberType = "gene";

    String identifierColName = util.toColNameOrEmpty(identifierVarSpec);
    String valueColName = util.toColNameOrEmpty(valueVarSpec);

    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    String nPCs = computeConfig.getNPCs() == null ? "2" : computeConfig.getNPCs().toString();

    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }
    String sampleEntityIdColName = VariableDef.toDotNotation(idColumns.get(0));

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting dimensionality reduction computation')");

      List<VariableSpec> computeInputVars = new ArrayList<>();
      computeInputVars.add(identifierVarSpec);
      computeInputVars.add(valueVarSpec);
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      StringBuilder lhsFormula = new StringBuilder();
      boolean firstIdCol = true;
      for (VariableDef idCol : idColumns) {
        if (firstIdCol) {
          lhsFormula.append("`").append(VariableDef.toDotNotation(idCol)).append("`");
          firstIdCol = false;
        } else {
          lhsFormula.append(" + `").append(VariableDef.toDotNotation(idCol)).append("`");
        }
      }

      connection.voidEval("abundanceData <- data.table::dcast(" + INPUT_DATA +
        ", " + lhsFormula + " ~ `" + identifierColName + "`" +
        ", value.var = " + singleQuote(valueColName) +
        ", fill = NA_real_)");

      // data.table::dcast sorts rows case-sensitively (ASCII order), but the subset
      // service uses a case-insensitive collation. Restore the original sample order
      // from INPUT_DATA so the PCA output matches the order the merge service expects.
      connection.voidEval("sampleOrder <- unique(" + INPUT_DATA + "[[" + singleQuote(sampleEntityIdColName) + "]])");
      connection.voidEval("abundanceData <- abundanceData[match(sampleOrder, abundanceData[[" + singleQuote(sampleEntityIdColName) + "]])]");

      // ancestorIdColumns excludes idColumns.get(0) since that is already recordIdColumn
      List<String> dotNotatedIdColumns = idColumns.stream().skip(1).map(VariableDef::toDotNotation).toList();
      StringBuilder dotNotatedIdColumnsString = new StringBuilder("c(");
      boolean first = true;
      for (String idCol : dotNotatedIdColumns) {
        if (first) {
          first = false;
          dotNotatedIdColumnsString.append(singleQuote(idCol));
        } else {
          dotNotatedIdColumnsString.append(",").append(singleQuote(idCol));
        }
      }
      dotNotatedIdColumnsString.append(")");

      connection.voidEval("abundDT <- microbiomeComputations::AbundanceData(" +
        "name=" + singleQuote(collectionMemberType) +
        ", data=abundanceData" +
        ", recordIdColumn=" + singleQuote(sampleEntityIdColName) +
        ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
        ", imputeZero=TRUE)");

      String normalize = computeConfig.getNormalize() != null && computeConfig.getNormalize() ? "TRUE" : "FALSE";

      connection.voidEval("pcaOutput <- veupathUtils::pca(abundDT, " +
        "nPCs=" + singleQuote(nPCs) + ", normalize=" + normalize + ", verbose=TRUE)");

      String dataCmd = "writeData(pcaOutput, NULL, TRUE)";
      String metaCmd = "writeMeta(pcaOutput, NULL, TRUE)";

      getWorkspace().writeDataResult(connection, dataCmd);
      getWorkspace().writeMetaResult(connection, metaCmd);
    });
  }
}
