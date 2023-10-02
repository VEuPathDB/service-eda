package org.veupathdb.service.eda.compute.plugins.correlationassaymetadata;

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
import org.veupathdb.service.eda.generated.model.CorrelationComputeConfig;
import org.veupathdb.service.eda.generated.model.CorrelationPluginRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

public class CorrelationAssayMetadataPlugin extends AbstractPlugin<CorrelationPluginRequest, CorrelationComputeConfig> {

  private static final String INPUT_DATA = "correlation_input";

  public CorrelationAssayMetadataPlugin(@NotNull PluginContext<CorrelationPluginRequest, CorrelationComputeConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    return List.of(new StreamSpec(INPUT_DATA, getConfig().getCollectionVariable1().getEntityId())
        .addVars(getUtil().getChildrenVariables(getConfig().getCollectionVariable1()))
      );
  }

  @Override
  protected void execute() {

    CorrelationComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();

    String entityId = computeConfig.getCollectionVariable1().getEntityId();
    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String method = computeConfig.getCorrelationMethod().getValue();

    // Get record id columns
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));
    
    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting correlation computation')");

      // Read in the abundance data
      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable1()));
      computeInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));
      connection.voidEval("abundanceData <- " + INPUT_DATA); // Renaming here so we can go get the sampleMetadata later

      // Read in the sample metadata
      // TO DO get only the continuous sample metadata!!
      // List<VariableSpec> sampleMetadataVars = ListBuilder.asList(comparisonVariableSpec);
      // sampleMetadataVars.add(computeEntityIdVarSpec);
      // connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, sampleMetadataVars));
      // connection.voidEval("sampleMetadata <- " + INPUT_DATA);
      // connection.voidEval("print(head(sampleMetadata))");


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


      // Set up input assay data.
      // TEMP until we have the continuous sample metadata, we're letting the abundance data sub for continuous sample metadata
      connection.voidEval("data1 <- AbundanceData(data=abundanceData" + 
                                                ", sampleMetadata=abundanceData" +
                                                ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                                ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                                ", imputeZero=TRUE)");

      connection.voidEval("data2 <- SampleMetadata(data = abundanceData[, 1:10]" +
                                                ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                                ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + "))");


      connection.voidEval("computeResult <- microbiomeComputations::correlation(data1=data1, data2=data2" +
                                                          ", method=" + singleQuote(method) +
                                                          ", verbose=TRUE)");


      String statsCmd = "writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
