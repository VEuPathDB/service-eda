package org.veupathdb.service.eda.compute.plugins.differentialexpression;

import org.gusdb.fgputil.ListBuilder;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.generated.model.LabeledRange;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionComputeConfig;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionPluginRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

public class DifferentialExpressionPlugin extends AbstractPlugin<DifferentialExpressionPluginRequest, DifferentialExpressionComputeConfig> {

  private static final String INPUT_DATA = "differential_expression_input";

  public DifferentialExpressionPlugin(@NotNull PluginContext<DifferentialExpressionPluginRequest, DifferentialExpressionComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    VariableSpec identifierVar = getConfig().getIdentifierVariable();
    VariableSpec valueVar = getConfig().getValueVariable();

    // Validate that identifier and value variables are on the same entity
    if (!identifierVar.getEntityId().equals(valueVar.getEntityId())) {
      throw new IllegalArgumentException(
        "Identifier variable and value variable must be on the same entity. " +
        "Got: " + identifierVar.getEntityId() + " and " + valueVar.getEntityId());
    }

    return List.of(new StreamSpec(INPUT_DATA, identifierVar.getEntityId())
        .addVar(identifierVar)                              // Gene ID column
        .addVar(valueVar)                                   // Count/expression column
        .addVar(getConfig().getComparator().getVariable())  // Comparator variable
      );
  }

  @Override
  protected void execute() {

    DifferentialExpressionComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();

    VariableSpec identifierVarSpec = computeConfig.getIdentifierVariable();
    VariableSpec valueVarSpec = computeConfig.getValueVariable();
    String entityId = identifierVarSpec.getEntityId();
    String collectionMemberType = "gene";  // Fixed for genomics data

    // Column names for tall format data
    String identifierColName = util.toColNameOrEmpty(identifierVarSpec);
    String valueColName = util.toColNameOrEmpty(valueVarSpec);

    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    String method = computeConfig.getDifferentialExpressionMethod().getValue().equals("DESeq") ? "DESeq"
      : (computeConfig.getDifferentialExpressionMethod().getValue().equals("limma") ? "limma" : "unknown");
    VariableSpec comparisonVariableSpec = computeConfig.getComparator().getVariable();
    String comparisonVariableDataShape = util.getVariableDataShape(comparisonVariableSpec);
    List<LabeledRange> groupA = computeConfig.getComparator().getGroupA();
    List<LabeledRange> groupB =  computeConfig.getComparator().getGroupB();
    String pValueFloor = computeConfig.getPValueFloor() != null ? computeConfig.getPValueFloor() : "1e-200"; // Same default as set in the frontend and microbiomeComputations

    // Get record id columns (ancestor entity IDs, ordered from immediate parent to root)
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }
    // The immediate parent entity (sample entity) is the record ID for the collection
    String sampleEntityIdColName = VariableDef.toDotNotation(idColumns.get(0));

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting differential expression computation')");

      // Read tall format data: gene ID, count, and ancestor ID columns (no gene entity obs ID needed)
      List<VariableSpec> computeInputVars = new ArrayList<>();
      computeInputVars.add(identifierVarSpec);  // Gene ID column
      computeInputVars.add(valueVarSpec);       // Count/expression column
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      // Build dcast formula: sampleID + higherAncestors... ~ geneIdColumn
      // Only use ancestor ID columns (sample entity ID etc.) as row identifiers — NOT the gene
      // entity observation ID (which is unique per row and would prevent the pivot collapsing
      // rows to one per sample).
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

      // Pivot from tall to wide: gene ID values become column names
      // Using data.table::dcast over tidyr::pivot_wider for performance reasons
      // (not specifically benchmarked in this context, but dcast is generally faster for large datasets)
      // Fill type depends on method: DESeq expects integers, limma expects reals
      String fillValue = method.equals("DESeq") ? "NA_integer_" : "NA_real_";
      connection.voidEval("countData <- data.table::dcast(" + INPUT_DATA +
                          ", " + lhsFormula + " ~ `" + identifierColName + "`" +
                          ", value.var = " + singleQuote(valueColName) +
                          ", fill = " + fillValue + ")");

      // data.table::dcast sorts rows case-sensitively (ASCII order), but the subset
      // service uses a case-insensitive collation. Restore the original sample order
      // from INPUT_DATA so countData matches the order the merge service expects.
      connection.voidEval("sampleOrder <- unique(" + INPUT_DATA + "[[" + singleQuote(sampleEntityIdColName) + "]])");
      connection.voidEval("countData <- countData[match(sampleOrder, countData[[" + singleQuote(sampleEntityIdColName) + "]])]");

      // Read in the sample metadata: comparator variable + sample entity ID
      List<VariableSpec> sampleMetadataVars = ListBuilder.asList(comparisonVariableSpec);
      sampleMetadataVars.add(idColumns.get(0));
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, sampleMetadataVars));
      // Deduplicate: tall format has one row per gene per sample; we need one row per sample
      connection.voidEval(INPUT_DATA + " <- unique(" + INPUT_DATA + ")");

      connection.voidEval("sampleMetadata <- veupathUtils::SampleMetadata(data = " + INPUT_DATA
                                + ", recordIdColumn = " + singleQuote(sampleEntityIdColName)
                                + ")");


      // Turn the list of id columns into an array of strings for R
      List<String> dotNotatedIdColumns = idColumns.stream().map(VariableDef::toDotNotation).toList();
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

      // Turn the comparator bin lists into a string for R
      String rGroupA = util.getRBinListAsString(groupA);
      String rGroupB = util.getRBinListAsString(groupB);


      // Create the comparator and input data objects
      connection.voidEval("comparator <- veupathUtils::Comparator(" +
                                "variable=veupathUtils::VariableMetadata(" +
                                  "variableSpec=veupathUtils::VariableSpec(" +
                                    "variableId='" + comparisonVariableSpec.getVariableId() + "'," +
                                    "entityId='" + comparisonVariableSpec.getEntityId() + "')," +
                                  "dataShape = veupathUtils::DataShape(value = '" + comparisonVariableDataShape + "')" +
                                ")," +
                                "groupA=" + rGroupA + "," +
                                "groupB=" + rGroupB +
                              ")");

      connection.voidEval("countDataCollection <- veupathUtils::CountDataCollection(name=" + singleQuote(collectionMemberType) +
                                                                          ", data=countData" +
                                                                          ", sampleMetadata=sampleMetadata" +
                                                                          ", recordIdColumn=" + singleQuote(sampleEntityIdColName) +
                                                                          ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                                                          ", imputeZero=TRUE)");

      
      connection.voidEval("computeResult <- veupathUtils::differentialExpression(" +
                                                          "collection=countDataCollection" +
                                                          ", comparator=comparator" +
                                                          ", method=" + singleQuote(method) +
                                                          ", pValueFloor=as.numeric(" + singleQuote(pValueFloor) + ")" +
                                                          ", verbose=TRUE)");


      String statsCmd = "writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
