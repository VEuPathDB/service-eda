package org.veupathdb.service.eda.compute.plugins.correlationassaymetadata;

import org.jetbrains.annotations.NotNull;
import org.veupathdb.service.eda.compute.plugins.PluginContext;
import org.veupathdb.service.eda.compute.plugins.PluginProvider;
import org.veupathdb.service.eda.compute.plugins.PluginQueue;
import org.veupathdb.service.eda.generated.model.Correlation1Collection;
import org.veupathdb.service.eda.generated.model.CorrelationComputeConfig;
import org.veupathdb.service.eda.generated.model.CorrelationAssayMetadataPluginRequest;
import org.veupathdb.service.eda.generated.model.CorrelationAssayMetadataPluginRequestImpl;

public class CorrelationAssayMetadataPluginProvider implements PluginProvider<CorrelationAssayMetadataPluginRequest, Correlation1Collection> {
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
  public Class<? extends CorrelationAssayMetadataPluginRequest> getRequestClass() {
    return CorrelationAssayMetadataPluginRequestImpl.class;
  }

  @NotNull
  @Override
  public CorrelationAssayMetadataPlugin createPlugin(@NotNull PluginContext<CorrelationAssayMetadataPluginRequest, Correlation1Collection> context) {
    return new CorrelationAssayMetadataPlugin(context);
  }
}
