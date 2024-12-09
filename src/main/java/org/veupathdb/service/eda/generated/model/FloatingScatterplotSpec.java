package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingScatterplotSpecImpl.class
)
public interface FloatingScatterplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("valueSpec")
  ValueSpecType getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpecType valueSpec);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("yAxisVariable")
  VariableSpec getYAxisVariable();

  @JsonProperty("yAxisVariable")
  void setYAxisVariable(VariableSpec yAxisVariable);

  @JsonProperty("overlayConfig")
  OverlayConfig getOverlayConfig();

  @JsonProperty("overlayConfig")
  void setOverlayConfig(OverlayConfig overlayConfig);

  @JsonProperty("maxAllowedDataPoints")
  Long getMaxAllowedDataPoints();

  @JsonProperty("maxAllowedDataPoints")
  void setMaxAllowedDataPoints(Long maxAllowedDataPoints);

  enum ValueSpecType {
    @JsonProperty("raw")
    RAW("raw"),

    @JsonProperty("smoothedMeanWithRaw")
    SMOOTHEDMEANWITHRAW("smoothedMeanWithRaw"),

    @JsonProperty("bestFitLineWithRaw")
    BESTFITLINEWITHRAW("bestFitLineWithRaw");

    public final String value;

    public String getValue() {
      return this.value;
    }

    ValueSpecType(String name) {
      this.value = name;
    }
  }
}
