package org.veupathdb.service.eda.compute.plugins.selfcorrelation;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.SelfCorrelationConfig;
import org.veupathdb.service.eda.generated.model.SelfCorrelationPluginRequest;
import org.veupathdb.service.eda.generated.model.SelfCorrelationPluginRequestImpl;

public class SelfCorrelationPluginProvider implements PluginProvider<SelfCorrelationPluginRequest, SelfCorrelationConfig> {
  @NotNull
  @Override
  public String getUrlSegment() {
    return "selfcorrelation";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Self Correlation Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Slow;
  }

  @NotNull
  @Override
  public Class<? extends SelfCorrelationPluginRequest> getRequestClass() {
    return SelfCorrelationPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public SelfCorrelationPlugin createPlugin(@NotNull PluginContext<SelfCorrelationPluginRequest, SelfCorrelationConfig> context) {
    return new SelfCorrelationPlugin(context);
  }
}
