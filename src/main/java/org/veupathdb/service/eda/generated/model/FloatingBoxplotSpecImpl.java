package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "xAxisVariable",
    "yAxisVariable",
    "overlayConfig",
    "maxAllowedDataPoints"
})
public class FloatingBoxplotSpecImpl implements FloatingBoxplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("yAxisVariable")
  private VariableSpec yAxisVariable;

  @JsonProperty("overlayConfig")
  private OverlayConfig overlayConfig;

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

  @JsonProperty("yAxisVariable")
  public VariableSpec getYAxisVariable() {
    return this.yAxisVariable;
  }

  @JsonProperty("yAxisVariable")
  public void setYAxisVariable(VariableSpec yAxisVariable) {
    this.yAxisVariable = yAxisVariable;
  }

  @JsonProperty("overlayConfig")
  public OverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(OverlayConfig overlayConfig) {
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
