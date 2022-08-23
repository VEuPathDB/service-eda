package org.veupathdb.service.eda.compute.plugins.betadiv;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
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
    // TODO: actually do something.
    getWorkspace().writeDataResult(getWorkspace().openStream(INPUT_DATA));

    // Copied from beta div data service plugin - need to rewrite basically.
    // String method = computeConfig.getBetaDivDistanceMethod().getValue();
    // VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(computeConfig.getCollectionVariable().getEntityId());
    // String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    // useRConnectionWithRemoteFiles(Resources.RSERVE_URL, dataStreams, connection -> {
      // List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      // computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      // connection.voidEval(util.getVoidEvalFreadCommand(COMPUTE_STREAM_NAME,
      //   computeInputVars
      // ));
      // connection.voidEval("print("+ COMPUTE_STREAM_NAME + ")");
      // connection.voidEval("betaDivDT <- betaDiv(" + COMPUTE_STREAM_NAME + ", " + 
      //                                                 singleQuote(computeEntityIdColName) + ", " + 
      //                                                 singleQuote(method) + ")");
    //   RServeClient.streamResult(connection, command, out);
    // });
  }
}
