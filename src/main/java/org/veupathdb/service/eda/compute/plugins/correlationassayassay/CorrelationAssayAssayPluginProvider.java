package org.veupathdb.service.eda.compute.plugins.correlationassayassay;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.Correlation2Collections;
import org.veupathdb.service.eda.generated.model.CorrelationComputeConfig;
import org.veupathdb.service.eda.generated.model.CorrelationAssayAssayPluginRequest;
import org.veupathdb.service.eda.generated.model.CorrelationAssayAssayPluginRequestImpl;

public class CorrelationAssayAssayPluginProvider implements PluginProvider<CorrelationAssayAssayPluginRequest, Correlation2Collections> {
  @NotNull
  @Override
  public String getUrlSegment() {
    return "correlationassayassay";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Correlation Assay Assay Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Slow;
  }

  @NotNull
  @Override
  public Class<? extends CorrelationAssayAssayPluginRequest> getRequestClass() {
    return CorrelationAssayAssayPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public CorrelationAssayAssayPlugin createPlugin(@NotNull PluginContext<CorrelationAssayAssayPluginRequest, Correlation2Collections> context) {
    return new CorrelationAssayAssayPlugin(context);
  }
}
