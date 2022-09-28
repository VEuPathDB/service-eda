package org.veupathdb.service.eda.compute.plugins.rankedabundance;

import org.gusdb.fgputil.ListBuilder;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.generated.model.RankedAbundancePluginConfig;
import org.veupathdb.service.eda.generated.model.RankedAbundancePluginRequest;
import org.veupathdb.service.eda.generated.model.VariableSpec;


import java.util.List;

public class RankedAbundancePlugin extends AbstractPlugin<RankedAbundancePluginRequest, RankedAbundancePluginConfig> {

  private static final String INPUT_DATA = "ranked_abundance_input";

  public RankedAbundancePlugin(@NotNull PluginContext<RankedAbundancePluginRequest, RankedAbundancePluginConfig> context) {
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
    
    RankedAbundancePluginConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(computeConfig.getCollectionVariable().getEntityId());
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String method = computeConfig.getRankingMethod().getName();
    
    RServe.useRConnection(connection -> {
      connection.voidEval("print('starting ranked abundance computation')");

      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      connection.voidEval("abundanceDT <- rankedAbundance(" + INPUT_DATA + ", " + 
                                                    util.singleQuote(computeEntityIdColName) + ", " + 
                                                    util.singleQuote(method) + ")");
      String dataCmd = "print(abundanceDT)";
      String metaCmd = "getMetadata(abundanceDT)";

      getWorkspace().writeDataResult(connection.eval(dataCmd).asString());
      getWorkspace().writeMetaResult(connection.eval(metaCmd).asString());
    });
  }
}
