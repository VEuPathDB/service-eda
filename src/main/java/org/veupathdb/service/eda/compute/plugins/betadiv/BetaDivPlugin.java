package org.veupathdb.service.eda.compute.plugins.betadiv;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.generated.model.BetaDivPluginConfig;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequest;


import java.util.List;

public class BetaDivPlugin extends AbstractPlugin<BetaDivPluginRequest, BetaDivPluginConfig> {

  private static final String INPUT_DATA = "example-input";
  // Ann figure out name. The following is from the DS abstract plugin
  // // shared stream name for plugins that need request only a single stream
  // protected static final String DEFAULT_SINGLE_STREAM_NAME = "single_tabular_dataset";

  public BetaDivPlugin(@NotNull PluginContext<BetaDivPluginRequest, BetaDivPluginConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    return List.of(new StreamSpec(INPUT_DATA, getConfig().getOutputEntityId()));
  }

  @Override
  protected void execute() {

    // Get parameters for the beta div computation
    
    BetaDivPluginConfig computeConfig = getConfig();

    // Next get what is called computeEntityIdColumnName in the data service plugins: basically 
    // the column name based on a variable (VariableDef). In the data service it goes like:
    // PluginUtil util = getUtil();
    // VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(computeConfig.getCollectionVariable().getEntityId());
    // String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    // For now using the appropriate column name for my fake data :)
    String computeEntityIdColName = "entity.SampleID";

    // Next we get the distance method from the config
    String distanceMethod = computeConfig.getBetaDivDistanceMethod().getName();
    
    RServe.useRConnection(connection -> {
      connection.voidEval("print('starting beta diversity computation')");
      
      // The data we want is based on all the children variables of the collection variable.
      // Assuming we have computeEntityIdVarSpec the variable spec of the collection variable, 
      // we do the folllowing in the ds to get all of the vars needed in the data:
      // List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      // computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));

      // Finally, read the data into Rserve.
      // Here we need the data (COMPUTE_STREAM_NAME) and the columns in the data we actually care about (computeInputVars)
      // connection.voidEval(util.getVoidEvalFreadCommand(COMPUTE_STREAM_NAME, computeInputVars));
      // For now, using a fake dataset that's automattically loaded into the env called "testOTU"
      connection.voidEval("df <- testOTU");
      
      // Run the R function that computes beta diversity. 
      // ANN - make new R functions that write the output dt to a tabular file and the metadata to a separate file.
      String command = "dt <- betaDiv(df, '" + computeEntityIdColName + "', method='" + distanceMethod + "', verbose=T)";
      connection.voidEval(command);
      
      // Eventually stream the results so that we can write them with writeDataResult and writeMetaResult
      // RServeClient.streamResult(command, out);
      
      // Temporary sanity check that the R function betaDiv is working :)
      connection.voidEval("print(dt)");
      
    });
    
    getWorkspace().writeDataResult(getWorkspace().openStream(INPUT_DATA));

  }
}
