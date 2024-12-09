package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CollectionFloatingBoxplotSpecImpl.class
)
public interface CollectionFloatingBoxplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("overlayConfig")
  CollectionOverlayConfig getOverlayConfig();

  @JsonProperty("overlayConfig")
  void setOverlayConfig(CollectionOverlayConfig overlayConfig);

  @JsonProperty("maxAllowedDataPoints")
  Long getMaxAllowedDataPoints();

  @JsonProperty("maxAllowedDataPoints")
  void setMaxAllowedDataPoints(Long maxAllowedDataPoints);
}
