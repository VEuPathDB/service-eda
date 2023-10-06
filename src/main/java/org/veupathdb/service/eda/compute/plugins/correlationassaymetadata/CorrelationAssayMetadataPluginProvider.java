package org.veupathdb.service.eda.compute.plugins.correlationassaymetadata;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.CorrelationComputeConfig;
import org.veupathdb.service.eda.generated.model.CorrelationPluginRequest;
import org.veupathdb.service.eda.generated.model.CorrelationPluginRequestImpl;

public class CorrelationAssayMetadataPluginProvider implements PluginProvider<CorrelationPluginRequest, CorrelationComputeConfig> {
  @NotNull
  @Override
  public String getUrlSegment() {
    return "correlationassaymetadata";
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Correlation Assay Metadata Plugin";
  }

  @NotNull
  @Override
  public PluginQueue getTargetQueue() {
    return PluginQueue.Slow;
  }

  @NotNull
  @Override
  public Class<? extends CorrelationPluginRequest> getRequestClass() {
    return CorrelationPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public CorrelationAssayMetadataPlugin createPlugin(@NotNull PluginContext<CorrelationPluginRequest, CorrelationComputeConfig> context) {
    return new CorrelationAssayMetadataPlugin(context);
  }
}
