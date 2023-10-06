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
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.CollectionSpec;

import java.io.InputStream;
import java.util.stream.Stream;
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
    // Get the collection variable and its entity
    CorrelationComputeConfig computeConfig = getConfig();
    CollectionSpec collectionVariable1 = computeConfig.getCollectionVariable1();
    String entityId = collectionVariable1.getEntityId();

    // Wrangle into correct types for what follows
    EntityDef entity = getContext().getReferenceMetadata().getEntity(entityId).orElseThrow();
    VariableSpec collectionVariable1VarSpec = VariableDef.newVariableSpec(entityId, collectionVariable1.getCollectionId());


    // Grab all continuous variabls from ancestors
    // The next line only grabs the second-up ancestor instead of all of them, because when i grabbed them all
    // the variables wold be duplicated in the output. I tried distinct() but that didn't work. Any ideas?
    // I also noticed we were receiving stable ids in the oriringal request, so i've filtered them out below. There 
    // is probably a nicer way to do this.
    // This at least works for now!
    Stream<VariableDef> descendantVariableStream = getContext().getReferenceMetadata().getAncestors(entity).get(1).getVariables().stream();
    List<VariableDef> metadataVariables = descendantVariableStream.filter(var -> var.getDataShape() == APIVariableDataShape.CONTINUOUS).filter(var -> !var.getVariableId().contains("_stable_id")).toList();


    return List.of(new StreamSpec(INPUT_DATA, getConfig().getCollectionVariable1().getEntityId())
        .addVars(getUtil().getChildrenVariables(collectionVariable1VarSpec))
        .addVars(metadataVariables)
      );
  }

  @Override
  protected void execute() {

    CorrelationComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    ReferenceMetadata meta = getContext().getReferenceMetadata();

    // Get compute parameters
    String method = computeConfig.getCorrelationMethod().getValue();
    CollectionSpec collectionVariable1 = computeConfig.getCollectionVariable1();
    String entityId = collectionVariable1.getEntityId();

    // Wrangle into helpful types
    EntityDef entity = meta.getEntity(entityId).orElseThrow();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(entityId);
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    VariableSpec collectionVariable1VarSpec = VariableDef.newVariableSpec(entityId, collectionVariable1.getCollectionId());

    // Get record id columns
    List<VariableDef> idColumns = new ArrayList<>();
    for (EntityDef ancestor : meta.getAncestors(entity)) {
      idColumns.add(ancestor.getIdColumnDef());
    }

    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));


    // Get stream of all ancestors and their variables
    Stream<VariableDef> descendantVariableStream = meta.getAncestors(entity).get(1).getVariables().stream();
    List<VariableDef> metadataVariables = descendantVariableStream.filter(var -> var.getDataShape() == APIVariableDataShape.CONTINUOUS).filter(var -> !var.getVariableId().contains("_stable_id")).toList();


    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting correlation computation')");

      // Read in the abundance data
      List<VariableSpec> abundanceInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      abundanceInputVars.addAll(util.getChildrenVariables(collectionVariable1VarSpec));
      abundanceInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, abundanceInputVars));
      connection.voidEval("abundanceData <- " + INPUT_DATA); // Renaming here so we can go get the sampleMetadata later

      // Read in the sample metadata
      List <VariableSpec> metadataInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      metadataInputVars.addAll(metadataVariables);
      metadataInputVars.addAll(idColumns);
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, metadataInputVars));
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

      
      
      // Format inputs for R
      connection.voidEval("data1 <- AbundanceData(data=abundanceData" + 
                                ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + ")" +
                                ", imputeZero=TRUE)");
      
      connection.voidEval("data2 <- SampleMetadata(data = sampleMetadata" +
                                ", recordIdColumn=" + singleQuote(computeEntityIdColName) +
                                ", ancestorIdColumns=as.character(" + dotNotatedIdColumnsString + "))");
      
      // Run correlation!
      connection.voidEval("computeResult <- microbiomeComputations::correlation(data1=data1, data2=data2" +
                                                          ", method=" + singleQuote(method) +
                                                          ", verbose=TRUE)");


      String statsCmd = "writeStatistics(computeResult, NULL, TRUE)";

      getWorkspace().writeStatisticsResult(connection, statsCmd);
    });
  }
}
