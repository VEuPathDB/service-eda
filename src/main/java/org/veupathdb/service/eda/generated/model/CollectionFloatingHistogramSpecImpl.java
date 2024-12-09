package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "barMode",
    "valueSpec",
    "overlayConfig",
    "binSpec",
    "viewport"
})
public class CollectionFloatingHistogramSpecImpl implements CollectionFloatingHistogramSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("barMode")
  private CollectionFloatingHistogramSpec.BarModeType barMode;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

  @JsonProperty("overlayConfig")
  private CollectionOverlayConfig overlayConfig;

  @JsonProperty("binSpec")
  private BinWidthSpec binSpec;

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

  @JsonProperty("barMode")
  public CollectionFloatingHistogramSpec.BarModeType getBarMode() {
    return this.barMode;
  }

  @JsonProperty("barMode")
  public void setBarMode(CollectionFloatingHistogramSpec.BarModeType barMode) {
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
  public CollectionOverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(CollectionOverlayConfig overlayConfig) {
    this.overlayConfig = overlayConfig;
  }

  @JsonProperty("binSpec")
  public BinWidthSpec getBinSpec() {
    return this.binSpec;
  }

  @JsonProperty("binSpec")
  public void setBinSpec(BinWidthSpec binSpec) {
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
