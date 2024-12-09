package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "barMode",
    "valueSpec",
    "xAxisVariable",
    "overlayConfig"
})
public class FloatingBarplotSpecImpl implements FloatingBarplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("barMode")
  private FloatingBarplotSpec.BarModeType barMode;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("overlayConfig")
  private OverlayConfig overlayConfig;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("barMode")
  public FloatingBarplotSpec.BarModeType getBarMode() {
    return this.barMode;
  }

  @JsonProperty("barMode")
  public void setBarMode(FloatingBarplotSpec.BarModeType barMode) {
    this.barMode = barMode;
  }

  @JsonProperty("valueSpec")
  public ValueSpec getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(ValueSpec valueSpec) {
    this.valueSpec = valueSpec;
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
  public OverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(OverlayConfig overlayConfig) {
    this.overlayConfig = overlayConfig;
  }
}
