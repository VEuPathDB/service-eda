package org.veupathdb.service.eda.compute.plugins.differentialabundance;

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
import org.veupathdb.service.eda.generated.model.BinRange;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceComputeConfig;
import org.veupathdb.service.eda.generated.model.DifferentialAbundancePluginRequest;
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
        .addVars(getUtil().getChildrenVariables(getConfig().getCollectionVariable()))
        .addVar(getConfig().getComparator().getVariable())
      );
  }

  @Override
  protected void execute() {

    DifferentialAbundanceComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();

    String entityId = computeConfig.getCollectionVariable().getEntityId();
    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String method = computeConfig.getDifferentialAbundanceMethod().getValue();
    VariableSpec comparisonVariableSpec = computeConfig.getComparator().getVariable();
    String comparisonVariable = util.toColNameOrEmpty(comparisonVariableSpec);
    List<BinRange> groupA = computeConfig.getComparator().getGroupA();
    List<BinRange> groupB =  computeConfig.getComparator().getGroupB();

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
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
      connection.voidEval("absoluteAbundanceData <- " + INPUT_DATA); // Renaming here so we can go get the sampleMetadata later

      // Read in the sample metadata
      List<VariableSpec> sampleMetadataVars = ListBuilder.asList(comparisonVariableSpec);
      sampleMetadataVars.add(computeEntityIdVarSpec);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, sampleMetadataVars));
      connection.voidEval("sampleMetadata <- " + INPUT_DATA);


      // Turn the list of id columns into an array of strings for R
      List<String> dotNotatedIdColumns = idColumns.stream().map(VariableDef::toDotNotation).toList();
      String dotNotatedIdColumnsString = "c(";
      boolean first = true;
      for (String idCol : dotNotatedIdColumns) {
        if (first) {
          first = false;
          dotNotatedIdColumnsString = dotNotatedIdColumnsString + singleQuote(idCol);
        } else {
          dotNotatedIdColumnsString = dotNotatedIdColumnsString + "," + singleQuote(idCol);
        }
      }
      dotNotatedIdColumnsString = dotNotatedIdColumnsString + ")";

      // Turn the comparator bin lists into a string for R
      String rGroupA = "veupathUtils::BinList(S4Vectors::SimpleList(";

      first = true;
      for (int i = 0; i < groupA.size(); i++) {
        System.out.println(groupA.get(i).getBinLabel());
        String rBin = "veupathUtils::Bin(binLabel='" + groupA.get(i).getBinLabel() + "'";
        if (groupA.get(i).getBinStart() != null) {
          rBin += ",binStart=" + String.valueOf(groupA.get(i).getBinStart()) + 
                  ",binEnd=" + String.valueOf(groupA.get(i).getBinEnd());
        }
        rBin += ")";
  
        if (first) {
          rGroupA += rBin;
          first = false;
        } else {
          rGroupA += "," + rBin;
        }
      }
  
      rGroupA += "))";
      System.out.println(rGroupA);

      // ... for group B
      String rGroupB = "veupathUtils::BinList(S4Vectors::SimpleList(";

      first = true;
      for (int i = 0; i < groupB.size(); i++) {
        System.out.println(groupB.get(i).getBinLabel());
        String rBin = "veupathUtils::Bin(binLabel='" + groupB.get(i).getBinLabel() + "'";
        if (groupB.get(i).getBinStart() != null) {
          rBin += ",binStart=" + String.valueOf(groupB.get(i).getBinStart()) + 
                  ",binEnd=" + String.valueOf(groupB.get(i).getBinEnd());
        }
        rBin += ")";
  
        if (first) {
          rGroupB += rBin;
          first = false;
        } else {
          rGroupB += "," + rBin;
        }
      }
  
      rGroupB += "))";
      System.out.println(rGroupB);

      // TEMP FOR TESTING ONLY - REMOVE WHEN ABSOLUTE ABUNDANCES ARE HERE
      connection.voidEval("taxaColNames <- names(absoluteAbundanceData[, -c('" + computeEntityIdColName + "', as.character(" + dotNotatedIdColumnsString + "))])");
      connection.voidEval("absoluteAbundanceData[, (taxaColNames) := lapply(.SD,function(x) {round(x*1000)}), .SDcols=taxaColNames]");
      // END OF TEMP FOR TESTING

      // Create the Comparator object. Combines a VariableMetadata object with two BinLists (groupA and groupB)
      // connection.voidEval("variable <- ...");
      System.out.println(comparisonVariableSpec.getVariableId());
      // connection.voidEval("print('microbiomeComputations::Comparator(" +
      //                           "variable = veupathUtils::VariableMetadata(" + 
      //                             "variableSpec = veupathUtils::VariableSpec(" +
      //                               "variableId=" + comparisonVariableSpec.getVariableId() +
      //                               ", entityId=" + comparisonVariableSpec.getEntityId() +
      //                             ")," +
      //                             "dataShape = veupathUtils::DataShape(value = 'CATEGORICAL')" +
      //                           ")))");

      connection.voidEval("comparator <- microbiomeComputations::Comparator(" +
                                "variable=veupathUtils::VariableMetadata(" + 
                                  "variableSpec=veupathUtils::VariableSpec(" +
                                    "variableId='" + comparisonVariableSpec.getVariableId() + "'," +
                                    "entityId='" + comparisonVariableSpec.getEntityId() + "')," +
                                  "dataShape = veupathUtils::DataShape(value = 'CATEGORICAL')" +
                                ")," +
                                "groupA=" + rGroupA + "," +
                                "groupB=" + rGroupB +
                              ")");


      connection.voidEval("inputData <- microbiomeComputations::AbsoluteAbundanceData(data=absoluteAbundanceData" + 
                                                                          ", sampleMetadata=sampleMetadata" +
                                                                          ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                                                          ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                                                          ", imputeZero=TRUE)");



      connection.voidEval("computeResult <- differentialAbundance(data=inputData" +
                                                          ", comparator=comparator" +
                                                          ", method=" + singleQuote(method) +
                                                          ", verbose=TRUE)");


      String statsCmd = "writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
