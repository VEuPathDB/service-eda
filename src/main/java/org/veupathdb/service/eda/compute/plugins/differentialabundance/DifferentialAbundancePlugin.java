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
        .addVar(getConfig().getComparisonVariable())
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
    System.out.println(computeEntityIdVarSpec);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    System.out.println(computeEntityIdColName);
    String method = computeConfig.getDifferentialAbundanceMethod().getValue();
    String groupA = "c('Female')";
    String groupB = "c('Male')";
    // String groupA = computeConfig.getDifferentialAbundanceGroupA() != null ? listToRVector(computeConfig.getDifferentialAbundanceGroupA()) : "NULL";
    // String groupB = computeConfig.getDifferentialAbundanceGroupB() != null ? listToRVector(computeConfig.getDifferentialAbundanceGroupB()) : "NULL";

    // Needs to be in a dot notation... like comptueEntityIdColName prolly
    VariableSpec comparisonVariableSpec = computeConfig.getComparisonVariable();
    String comparisonVariable = util.toColNameOrEmpty(comparisonVariableSpec);
    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }
    
    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting differential abundance computation')");

      // Read in the abundance data
      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
      connection.voidEval("absoluteAbundanceData <- " + INPUT_DATA); // Need to rename so we can go get the sampleMetadata later

      // Read in the sample metadata
      List<VariableSpec> sampleMetadataVars = ListBuilder.asList(comparisonVariableSpec);
      sampleMetadataVars.add(computeEntityIdVarSpec);
      System.out.println(sampleMetadataVars);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, sampleMetadataVars));
      connection.voidEval("sampleMetadata <- " + INPUT_DATA);


      // TODO make a helper for this i think
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

      // TEMP FOR TESTING ONLY - REMOVE WHEN ABSOLUTE ABUNDANCES ARE HERE
      connection.voidEval("taxaColNames <- names(absoluteAbundanceData[, -c('" + computeEntityIdColName + "', as.character(" + dotNotatedIdColumnsString + "))])");
      connection.voidEval("absoluteAbundanceData[, (taxaColNames) := lapply(.SD,function(x) {round(x*1000)}), .SDcols=taxaColNames]");
      // END OF TEMP FOR TESTING

      connection.voidEval("diffabundDT <- microbiomeComputations::AbsoluteAbundanceData(data=absoluteAbundanceData" + 
                                                                          ", sampleMetadata=sampleMetadata" +
                                                                          ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                                                          ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                                                          ", imputeZero=TRUE)");


      connection.voidEval("differentialabundanceDT <- differentialAbundance(data=diffabundDT" +
                                                          ", comparisonVariable=" + singleQuote(comparisonVariable) +
                                                          ", groupA=" + groupA +
                                                          ", groupB=" + groupB + 
                                                          ", method=" + singleQuote(method) +
                                                          ", verbose=TRUE)");



      String dataCmd = "writeData(differentialabundanceDT, NULL, TRUE)";
      String metaCmd = "writeMeta(differentialabundanceDT, NULL, TRUE)";
      String statsCmd = "writeStatistics(differentialabundanceDT, NULL, TRUE)";

      getWorkspace().writeDataResult(connection, dataCmd);
      getWorkspace().writeMetaResult(connection, metaCmd);
      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
