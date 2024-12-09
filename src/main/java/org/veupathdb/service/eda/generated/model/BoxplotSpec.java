package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BoxplotSpecImpl.class
)
public interface BoxplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("showMissingness")
  ShowMissingnessNoAxes getShowMissingness();

  @JsonProperty("showMissingness")
  void setShowMissingness(ShowMissingnessNoAxes showMissingness);

  @JsonProperty("points")
  PointsType getPoints();

  @JsonProperty("points")
  void setPoints(PointsType points);

  @JsonProperty("mean")
  StringBoolean getMean();

  @JsonProperty("mean")
  void setMean(StringBoolean mean);

  @JsonProperty("computeStats")
  StringBoolean getComputeStats();

  @JsonProperty("computeStats")
  void setComputeStats(StringBoolean computeStats);

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

  enum PointsType {
    @JsonProperty("outliers")
    OUTLIERS("outliers"),

    @JsonProperty("all")
    ALL("all");

    public final String value;

    public String getValue() {
      return this.value;
    }

    PointsType(String name) {
      this.value = name;
    }
  }
}
