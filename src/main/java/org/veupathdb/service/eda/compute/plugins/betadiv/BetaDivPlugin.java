package org.veupathdb.service.eda.compute.plugins.betadiv;

import org.gusdb.fgputil.ListBuilder;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.generated.model.BetaDivPluginConfig;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;


import java.util.List;

public class BetaDivPlugin extends AbstractPlugin<BetaDivPluginRequest, BetaDivPluginConfig> {

  private static final String INPUT_DATA = "beta_div_input";

  public BetaDivPlugin(@NotNull PluginContext<BetaDivPluginRequest, BetaDivPluginConfig> context) {
    super(context);
  }

  @NotNull
  @Override
  public List<StreamSpec> getStreamSpecs() {
    return List.of(new StreamSpec(INPUT_DATA, getConfig().getCollectionVariable().getEntityId())
        .addVars(getUtil().getChildrenVariables(getConfig().getCollectionVariable())
      ));
  }

  @Override
  protected void execute() {
    
    BetaDivPluginConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(computeConfig.getCollectionVariable().getEntityId());
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String distanceMethod = computeConfig.getBetaDivDistanceMethod().getName();
    
    RServe.useRConnection(connection -> {
      connection.voidEval("print('starting beta diversity computation')");

      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      connection.voidEval("betaDivDT <- betaDiv(" + INPUT_DATA + ", " + 
                                                    util.singleQuote(computeEntityIdColName) + ", " + 
                                                    util.singleQuote(distanceMethod) + ")");
      String dataCmd = "print(betaDivDT)";
      String metaCmd = "getMetadata(betaDivDT)";

      getWorkspace().writeDataResult(connection.eval(dataCmd).asString());
      getWorkspace().writeMetaResult(connection.eval(metaCmd).asString());
    });
  }
}
