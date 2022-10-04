package org.veupathdb.service.eda.compute.plugins.betadiv;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.AbstractPlugin;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.BetaDivPluginConfig;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequestImpl;

public class BetaDivPluginProvider implements PluginProvider<BetaDivPluginRequest, BetaDivPluginConfig> {
  @NotNull
  @Override
  public String getUrlSegment() {
    return "betadiv";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Beta Diversity Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Slow;
  }

  @NotNull
  @Override
  public Class<? extends BetaDivPluginRequest> getRequestClass() {
    return BetaDivPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public BetaDivPlugin createPlugin(@NotNull PluginContext<BetaDivPluginRequest, BetaDivPluginConfig> context) {
    return new BetaDivPlugin(context);
  }
}
