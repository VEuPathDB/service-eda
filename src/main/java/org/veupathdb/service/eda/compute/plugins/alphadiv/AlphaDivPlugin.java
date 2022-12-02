package org.veupathdb.service.eda.compute.plugins.alphadiv;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gusdb.fgputil.ListBuilder;
import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.RServe;
import org.veupathdb.service.eda.generated.model.AlphaDivComputeConfig;
import org.veupathdb.service.eda.generated.model.AlphaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.ComputedVariableMetadata;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class AlphaDivPlugin extends AbstractPlugin<AlphaDivPluginRequest, AlphaDivComputeConfig> {

  private static final String INPUT_DATA = "alpha_div_input";

  public AlphaDivPlugin(@NotNull PluginContext<AlphaDivPluginRequest, AlphaDivComputeConfig> context) {
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

    AlphaDivComputeConfig computeConfig = getConfig();
    PluginUtil util = getUtil();
    VariableDef computeEntityIdVarSpec = util.getEntityIdVarSpec(computeConfig.getCollectionVariable().getEntityId());
    String computeEntityIdColName = util.toColNameOrEmpty(computeEntityIdVarSpec);
    String method = computeConfig.getAlphaDivMethod().getName();
    HashMap<String, InputStream> dataStream = new HashMap<>();
    dataStream.put(INPUT_DATA, getWorkspace().openStream(INPUT_DATA));

    RServe.useRConnectionWithRemoteFiles(dataStream, connection -> {
      connection.voidEval("print('starting alpha diversity computation')");

      List<VariableSpec> computeInputVars = ListBuilder.asList(computeEntityIdVarSpec);
      computeInputVars.addAll(util.getChildrenVariables(computeConfig.getCollectionVariable()));
      connection.voidEval(util.getVoidEvalFreadCommand(INPUT_DATA, computeInputVars));

      connection.voidEval("abundDT <- microbiomeComputations::AbundanceData(data=" + INPUT_DATA + ",recordIdColumn=" + PluginUtil.singleQuote(computeEntityIdColName) + ",imputeZero=TRUE)");
      connection.voidEval("alphaDivDT <- alphaDiv(abundDT, " +
                                                  PluginUtil.singleQuote(method) + ", TRUE)");
      String dataCmd = "writeData(alphaDivDT, NULL, TRUE)";
      String metaCmd = "writeMeta(alphaDivDT, NULL, TRUE)";

      getWorkspace().writeDataResult(connection, dataCmd);
      getWorkspace().writeMetaResult(connection, metaCmd);
    });
  }
}
