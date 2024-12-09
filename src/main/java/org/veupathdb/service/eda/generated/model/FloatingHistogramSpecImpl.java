package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "xAxisVariable",
    "barMode",
    "valueSpec",
    "overlayConfig",
    "binSpec",
    "viewport"
})
public class FloatingHistogramSpecImpl implements FloatingHistogramSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("barMode")
  private FloatingHistogramSpec.BarModeType barMode;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

  @JsonProperty("overlayConfig")
  private OverlayConfig overlayConfig;

  @JsonProperty("binSpec")
  private BinSpec binSpec;

  @JsonProperty("viewport")
  private NumericViewport viewport;

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

  @JsonProperty("barMode")
  public FloatingHistogramSpec.BarModeType getBarMode() {
    return this.barMode;
  }

  @JsonProperty("barMode")
  public void setBarMode(FloatingHistogramSpec.BarModeType barMode) {
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

  @JsonProperty("overlayConfig")
  public OverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(OverlayConfig overlayConfig) {
    this.overlayConfig = overlayConfig;
  }

  @JsonProperty("binSpec")
  public BinSpec getBinSpec() {
    return this.binSpec;
  }

  @JsonProperty("binSpec")
  public void setBinSpec(BinSpec binSpec) {
    this.binSpec = binSpec;
  }

  @JsonProperty("viewport")
  public NumericViewport getViewport() {
    return this.viewport;
  }

  @JsonProperty("viewport")
  public void setViewport(NumericViewport viewport) {
    this.viewport = viewport;
  }
}
