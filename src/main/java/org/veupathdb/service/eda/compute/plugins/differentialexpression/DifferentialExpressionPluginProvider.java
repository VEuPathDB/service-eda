package org.veupathdb.service.eda.compute.plugins.differentialexpression;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionComputeConfig;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionPluginRequest;
import org.veupathdb.service.eda.generated.model.DifferentialExpressionPluginRequestImpl;

public class DifferentialExpressionPluginProvider implements PluginProvider<DifferentialExpressionPluginRequest, DifferentialExpressionComputeConfig> {
  @NotNull
  @Override
  public String getUrlSegment() {
    return "differentialexpression";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Differential Expression Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Slow;
  }

  @NotNull
  @Override
  public Class<? extends DifferentialExpressionPluginRequest> getRequestClass() {
    return DifferentialExpressionPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public DifferentialExpressionPlugin createPlugin(@NotNull PluginContext<DifferentialExpressionPluginRequest, DifferentialExpressionComputeConfig> context) {
    return new DifferentialExpressionPlugin(context);
  }
}
