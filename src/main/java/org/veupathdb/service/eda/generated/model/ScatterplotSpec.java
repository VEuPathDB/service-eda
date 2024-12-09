package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ScatterplotSpecImpl.class
)
public interface ScatterplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("showMissingness")
  ShowMissingnessNoAxes getShowMissingness();

  @JsonProperty("showMissingness")
  void setShowMissingness(ShowMissingnessNoAxes showMissingness);

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

  @JsonProperty("overlayVariable")
  VariableSpec getOverlayVariable();

  @JsonProperty("overlayVariable")
  void setOverlayVariable(VariableSpec overlayVariable);

  @JsonProperty("facetVariable")
  List<VariableSpec> getFacetVariable();

  @JsonProperty("facetVariable")
  void setFacetVariable(List<VariableSpec> facetVariable);

  @JsonProperty("maxAllowedDataPoints")
  Long getMaxAllowedDataPoints();

  @JsonProperty("maxAllowedDataPoints")
  void setMaxAllowedDataPoints(Long maxAllowedDataPoints);

  @JsonProperty("correlationMethod")
  ScatterCorrelationMethod getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(ScatterCorrelationMethod correlationMethod);

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
