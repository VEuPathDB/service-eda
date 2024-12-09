package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingHistogramSpecImpl.class
)
public interface FloatingHistogramSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("barMode")
  BarModeType getBarMode();

  @JsonProperty("barMode")
  void setBarMode(BarModeType barMode);

  @JsonProperty("valueSpec")
  ValueSpec getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpec valueSpec);

  @JsonProperty("overlayConfig")
  OverlayConfig getOverlayConfig();

  @JsonProperty("overlayConfig")
  void setOverlayConfig(OverlayConfig overlayConfig);

  @JsonProperty("binSpec")
  BinSpec getBinSpec();

  @JsonProperty("binSpec")
  void setBinSpec(BinSpec binSpec);

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
