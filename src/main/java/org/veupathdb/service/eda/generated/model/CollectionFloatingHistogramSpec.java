package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CollectionFloatingHistogramSpecImpl.class
)
public interface CollectionFloatingHistogramSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("barMode")
  BarModeType getBarMode();

  @JsonProperty("barMode")
  void setBarMode(BarModeType barMode);

  @JsonProperty("valueSpec")
  ValueSpec getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpec valueSpec);

  @JsonProperty("overlayConfig")
  CollectionOverlayConfig getOverlayConfig();

  @JsonProperty("overlayConfig")
  void setOverlayConfig(CollectionOverlayConfig overlayConfig);

  @JsonProperty("binSpec")
  BinWidthSpec getBinSpec();

  @JsonProperty("binSpec")
  void setBinSpec(BinWidthSpec binSpec);

  @JsonProperty("viewport")
  NumericViewport getViewport();

  @JsonProperty("viewport")
  void setViewport(NumericViewport viewport);

  enum BarModeType {
    @JsonProperty("overlay")
    OVERLAY("overlay"),

    @JsonProperty("stack")
    STACK("stack");

    public final String value;

    public String getValue() {
      return this.value;
    }

    BarModeType(String name) {
      this.value = name;
    }
  }
}