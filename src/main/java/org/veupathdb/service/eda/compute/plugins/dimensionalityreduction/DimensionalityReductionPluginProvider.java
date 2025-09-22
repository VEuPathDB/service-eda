package org.veupathdb.service.eda.compute.plugins.dimensionalityreduction;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.DimensionalityReductionComputeConfig;
import org.veupathdb.service.eda.generated.model.DimensionalityReductionPluginRequest;
import org.veupathdb.service.eda.generated.model.DimensionalityReductionPluginRequestImpl;

public class DimensionalityReductionPluginProvider implements PluginProvider<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> {

  @NotNull
  @Override
  public String getUrlSegment() {
    return "dimensionalityreduction";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Dimensionality Reduction Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Fast;
  }

  @NotNull
  @Override
  public Class<? extends DimensionalityReductionPluginRequest> getRequestClass() {
    return DimensionalityReductionPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public DimensionalityReductionPlugin createPlugin(@NotNull PluginContext<DimensionalityReductionPluginRequest, DimensionalityReductionComputeConfig> context) {
    return new DimensionalityReductionPlugin(context);
  }
}
