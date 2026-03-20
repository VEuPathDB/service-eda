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
import org.veupathdb.service.eda.generated.model.DataFormat;
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
  private static final String SAMPLE_DATA = "sample_entity_input";

  public DimensionalityReductionPlugin(@NotNull PluginContext<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    VariableSpec identifierVar = getConfig().getIdentifierVariable();
    VariableSpec valueVar = getConfig().getValueVariable();

    // Determine sample entity (immediate parent of gene entity)
    ReferenceMetadata meta = getContext().getReferenceMetadata();
    EntityDef geneEntity = meta.getEntity(identifierVar.getEntityId()).orElseThrow();
    String sampleEntityId = meta.getAncestors(geneEntity).get(0).getId();

    return List.of(
      new StreamSpec(INPUT_DATA, identifierVar.getEntityId())
        .addVar(identifierVar)
        .addVar(valueVar),
      new StreamSpec(SAMPLE_DATA, sampleEntityId)  // just ID columns, no extra vars
    );
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
    boolean isRawCounts = computeConfig.getDataFormat() == DataFormat.RAWCOUNTS;

    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }
    String sampleEntityIdColName = VariableDef.toDotNotation(idColumns.get(0));

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));
    dataStream.put(SAMPLE_DATA, getWorkspace().openStream(SAMPLE_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting dimensionality reduction computation')");

      List<VariableSpec> computeInputVars = new ArrayList<>();
      computeInputVars.add(identifierVarSpec);
      computeInputVars.add(valueVarSpec);
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      // Read sample entity stream (only ID columns)
      connection.voidEval(util.getVoidEvalFreadCommand(SAMPLE_DATA, idColumns.toArray(new VariableSpec[0])));

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

      // Fill type depends on data format: raw counts are integers, normalized values are reals.
      // fun.aggregate handles genes shared across multiple arrays (e.g. control probes):
      // for raw counts we round the mean back to integer; for normalized values plain mean.
      String fillValue = isRawCounts ? "NA_integer_" : "NA_real_";
      String funAggregate = isRawCounts ? "function(x) as.integer(round(mean(x, na.rm=TRUE)))" : "function(x) mean(x, na.rm=TRUE)";
      connection.voidEval("inputData <- data.table::dcast(" + INPUT_DATA +
        ", " + lhsFormula + " ~ `" + identifierColName + "`" +
        ", value.var = " + singleQuote(valueColName) +
        ", fill = " + fillValue +
        ", fun.aggregate = " + funAggregate + ")");

      // data.table::dcast sorts rows case-sensitively (ASCII order), but the subset
      // service uses a case-insensitive collation. Restore the original sample order
      // from INPUT_DATA so the PCA output matches the order the merge service expects.
      // We use the sample entity stream order which includes ALL samples (even those
      // with no gene data), not just the ones present in the gene entity stream.
      connection.voidEval("inputData <- inputData[match(" + SAMPLE_DATA + "[[" +
        singleQuote(sampleEntityIdColName) + "]], inputData[[" + singleQuote(sampleEntityIdColName) + "]])]");

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

      // Use CountDataCollection for raw counts (validates integers, imputes zeros)
      // Use ArrayDataCollection for normalized values (accepts continuous data, no imputation)
      String collectionClass = isRawCounts ? "CountDataCollection" : "ArrayDataCollection";
      String imputeZero = isRawCounts ? "TRUE" : "FALSE";
      connection.voidEval("dataCollection <- veupathUtils::" + collectionClass + "(" +
        "name=" + singleQuote(collectionMemberType) +
        ", data=inputData" +
        ", recordIdColumn=" + singleQuote(sampleEntityIdColName) +
        ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
        ", imputeZero=" + imputeZero + ")");

      // Normalize raw counts with DESeq2 median-of-ratios; pre-normalized data skips this
      String normalize = isRawCounts ? "TRUE" : "FALSE";

      connection.voidEval("pcaOutput <- veupathUtils::pca(dataCollection, " +
        "nPCs=" + singleQuote(nPCs) + ", normalize=" + normalize + ", verbose=TRUE)");

      // Left-join PCA output onto full sample list so samples with no gene data get NA PC values.
      // This ensures the compute output has a row for every sample, matching what the merge service expects.
      connection.voidEval("pcaData <- pcaOutput@data");
      connection.voidEval("pcaData <- merge(" + SAMPLE_DATA + ", pcaData, by=names(" + SAMPLE_DATA + "), all.x=TRUE, sort=FALSE)");
      // merge may reorder rows; restore sample stream order
      connection.voidEval("pcaData <- pcaData[match(" + SAMPLE_DATA + "[[" +
        singleQuote(sampleEntityIdColName) + "]], pcaData[[" + singleQuote(sampleEntityIdColName) + "]])]");
      connection.voidEval("pcaOutput@data <- pcaData");

      String dataCmd = "writeData(pcaOutput, NULL, TRUE)";
      String metaCmd = "writeMeta(pcaOutput, NULL, TRUE)";

      getWorkspace().writeDataResult(connection, dataCmd);
      getWorkspace().writeMetaResult(connection, metaCmd);
    });
  }
}
