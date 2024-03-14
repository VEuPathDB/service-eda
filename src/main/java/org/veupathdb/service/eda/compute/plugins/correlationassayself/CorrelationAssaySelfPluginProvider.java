package org.veupathdb.service.eda.compute.plugins.correlationassayself;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.CorrelationAssaySelfConfig;
import org.veupathdb.service.eda.generated.model.CorrelationAssaySelfPluginRequest;
import org.veupathdb.service.eda.generated.model.CorrelationAssaySelfPluginRequestImpl;

public class CorrelationAssaySelfPluginProvider implements PluginProvider<CorrelationAssaySelfPluginRequest, CorrelationAssaySelfConfig> {
  @NotNull
  @Override
  public String getUrlSegment() {
    return "correlationassayself";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Correlation Assay Self Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Slow;
  }

  @NotNull
  @Override
  public Class<? extends CorrelationAssaySelfPluginRequest> getRequestClass() {
    return CorrelationAssaySelfPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public CorrelationAssaySelfPlugin createPlugin(@NotNull PluginContext<CorrelationAssaySelfPluginRequest, CorrelationAssaySelfConfig> context) {
    return new CorrelationAssaySelfPlugin(context);
  }
}
