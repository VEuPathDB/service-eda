package org.veupathdb.service.eda.compute.plugins.differentialabundance;

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
import org.veupathdb.service.eda.generated.model.LabeledRange;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceComputeConfig;
import org.veupathdb.service.eda.generated.model.DifferentialAbundancePluginRequest;
import org.veupathdb.service.eda.generated.model.CollectionSpec;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

public class DifferentialAbundancePlugin extends AbstractPlugin<DifferentialAbundancePluginRequest, DifferentialAbundanceComputeConfig> {

  private static final String INPUT_DATA = "differential_abundance_input";

  public DifferentialAbundancePlugin(@NotNull PluginContext<DifferentialAbundancePluginRequest, DifferentialAbundanceComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    return List.of(new StreamSpec(INPUT_DATA, getConfig().getCollectionVariable().getEntityId())
        .addVars(getUtil().getCollectionMembers(getConfig().getCollectionVariable()))
        .addVar(getConfig().getComparator().getVariable())
      );
  }

  @Override
  protected void execute() {

    DifferentialAbundanceComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();

    CollectionSpec collectionSpec = computeConfig.getCollectionVariable();
    CollectionDef collection = meta.getCollection(collectionSpec).orElseThrow();
    String collectionMemberType = collection.getMember() == null ? "unknown" : collection.getMember();
    String entityId = collectionSpec.getEntityId();
    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    // if we ever introduce ANCOM-BC or something else we'll need to change this
    String method = computeConfig.getDifferentialAbundanceMethod().getValue().equals("Maaslin") ? "Maaslin2" : "DESeq2";
    VariableSpec comparisonVariableSpec = computeConfig.getComparator().getVariable();
    String comparisonVariableDataShape = util.getVariableDataShape(comparisonVariableSpec);
    List<LabeledRange> groupA = computeConfig.getComparator().getGroupA();
    List<LabeledRange> groupB =  computeConfig.getComparator().getGroupB();
    String pValueFloor = computeConfig.getPValueFloor() != null ? computeConfig.getPValueFloor() : "1e-200"; // Same default as set in the frontend and microbiomeComputations

    // Get record id columns
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting differential abundance computation')");

      // Read in the abundance data
      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getCollectionMembers(collectionSpec));
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
      connection.voidEval("abundanceData <- " + INPUT_DATA); // Renaming here so we can go get the sampleMetadata later

      // Read in the sample metadata
      List<VariableSpec> sampleMetadataVars = ListBuilder.asList(comparisonVariableSpec);
      sampleMetadataVars.add(computeEntityIdVarSpec);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, sampleMetadataVars));
      connection.voidEval("sampleMetadata <- veupathUtils::SampleMetadata(data = " + INPUT_DATA
                                + ", recordIdColumn = " + singleQuote(computeEntityIdColName)
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
      connection.voidEval("comparator <- microbiomeComputations::Comparator(" +
                                "variable=veupathUtils::VariableMetadata(" +
                                  "variableSpec=veupathUtils::VariableSpec(" +
                                    "variableId='" + comparisonVariableSpec.getVariableId() + "'," +
                                    "entityId='" + comparisonVariableSpec.getEntityId() + "')," +
                                  "dataShape = veupathUtils::DataShape(value = '" + comparisonVariableDataShape + "')" +
                                ")," +
                                "groupA=" + rGroupA + "," +
                                "groupB=" + rGroupB +
                              ")");

      String abundanceDataClass = "AbundanceData";
      if (util.getCollectionNormalizationMethod(collectionSpec).equals("NULL") &&
          util.getCollectionIsCompositional(collectionSpec) &&
          !util.getCollectionIsProportion(collectionSpec))
      {
        abundanceDataClass = "AbsoluteAbundanceData";
      }
      connection.voidEval("inputData <- microbiomeComputations::" + abundanceDataClass + "(name=" + singleQuote(collectionMemberType) +
                                                                          ", data=abundanceData" +
                                                                          ", sampleMetadata=sampleMetadata" +
                                                                          ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                                                          ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                                                          ", imputeZero=TRUE)");

      // there is also a user-facing differentialAbundance method in MicrobiomeDB r package
      // we dont want to use that one, were using the internal one directly. the api is more explicit.
      connection.voidEval("computeResult <- microbiomeComputations::internalDiffAbund(data=inputData" +
                                                          ", comparator=comparator" +
                                                          ", method=" + singleQuote(method) +
                                                          ", pValueFloor=as.numeric(" + singleQuote(pValueFloor) + ")" +
                                                          ", verbose=TRUE)");


      String statsCmd = "writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
