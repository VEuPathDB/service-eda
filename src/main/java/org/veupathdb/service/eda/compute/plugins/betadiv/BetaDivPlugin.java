package org.veupathdb.service.eda.compute.plugins.betadiv;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gusdb.fgputil.ListBuilder;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.generated.model.BetaDivComputeConfig;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.ComputedVariableMetadata;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class BetaDivPlugin extends AbstractPlugin<BetaDivPluginRequest, BetaDivComputeConfig> {

  private static final String INPUT_DATA = "beta_div_input";

  public BetaDivPlugin(@NotNull PluginContext<BetaDivPluginRequest, BetaDivComputeConfig> context) {
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

    BetaDivComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(computeConfig.getCollectionVariable().getEntityId());
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String distanceMethod = computeConfig.getBetaDivDistanceMethod().getName();
    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));
    
    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting beta diversity computation')");

      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      connection.voidEval("betaDivDT <- betaDiv(" + INPUT_DATA + ", " +
                                                    PluginUtil.singleQuote(computeEntityIdColName) + ", " +
                                                    PluginUtil.singleQuote(distanceMethod) + ")");
      String dataCmd = "readr::format_tsv(betaDivDT)";
      String metaCmd = "getMetadata(betaDivDT)";

      // FIXME: should not be storing off tabular result in memory!!
      getWorkspace().writeDataResult(connection.eval(dataCmd).asString());
      getWorkspace().writeMetaResult(new ObjectMapper().readValue(connection.eval(metaCmd).asString(), ComputedVariableMetadata.class));
    });
  }
}
