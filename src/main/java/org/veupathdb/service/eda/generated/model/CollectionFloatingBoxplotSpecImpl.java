package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "xAxisVariable",
    "overlayConfig",
    "maxAllowedDataPoints"
})
public class CollectionFloatingBoxplotSpecImpl implements CollectionFloatingBoxplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("overlayConfig")
  private CollectionOverlayConfig overlayConfig;

  @JsonProperty("maxAllowedDataPoints")
  private Long maxAllowedDataPoints;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("xAxisVariable")
  public VariableSpec getXAxisVariable() {
    return this.xAxisVariable;
  }

  @JsonProperty("xAxisVariable")
  public void setXAxisVariable(VariableSpec xAxisVariable) {
    this.xAxisVariable = xAxisVariable;
  }

  @JsonProperty("overlayConfig")
  public CollectionOverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(CollectionOverlayConfig overlayConfig) {
    this.overlayConfig = overlayConfig;
  }

  @JsonProperty("maxAllowedDataPoints")
  public Long getMaxAllowedDataPoints() {
    return this.maxAllowedDataPoints;
  }

  @JsonProperty("maxAllowedDataPoints")
  public void setMaxAllowedDataPoints(Long maxAllowedDataPoints) {
    this.maxAllowedDataPoints = maxAllowedDataPoints;
  }
}
