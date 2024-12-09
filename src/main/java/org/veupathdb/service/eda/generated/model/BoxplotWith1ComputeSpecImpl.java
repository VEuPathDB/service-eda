package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "showMissingness",
    "points",
    "mean",
    "computeStats",
    "xAxisVariable",
    "overlayVariable",
    "facetVariable"
})
public class BoxplotWith1ComputeSpecImpl implements BoxplotWith1ComputeSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("showMissingness")
  private ShowMissingnessNoAxes showMissingness;

  @JsonProperty("points")
  private BoxplotWith1ComputeSpec.PointsType points;

  @JsonProperty("mean")
  private StringBoolean mean;

  @JsonProperty("computeStats")
  private StringBoolean computeStats;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("overlayVariable")
  private VariableSpec overlayVariable;

  @JsonProperty("facetVariable")
  private List<VariableSpec> facetVariable;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("showMissingness")
  public ShowMissingnessNoAxes getShowMissingness() {
    return this.showMissingness;
  }

  @JsonProperty("showMissingness")
  public void setShowMissingness(ShowMissingnessNoAxes showMissingness) {
    this.showMissingness = showMissingness;
  }

  @JsonProperty("points")
  public BoxplotWith1ComputeSpec.PointsType getPoints() {
    return this.points;
  }

  @JsonProperty("points")
  public void setPoints(BoxplotWith1ComputeSpec.PointsType points) {
    this.points = points;
  }

  @JsonProperty("mean")
  public StringBoolean getMean() {
    return this.mean;
  }

  @JsonProperty("mean")
  public void setMean(StringBoolean mean) {
    this.mean = mean;
  }

  @JsonProperty("computeStats")
  public StringBoolean getComputeStats() {
    return this.computeStats;
  }

  @JsonProperty("computeStats")
  public void setComputeStats(StringBoolean computeStats) {
    this.computeStats = computeStats;
  }

  @JsonProperty("xAxisVariable")
  public VariableSpec getXAxisVariable() {
    return this.xAxisVariable;
  }

  @JsonProperty("xAxisVariable")
  public void setXAxisVariable(VariableSpec xAxisVariable) {
    this.xAxisVariable = xAxisVariable;
  }

  @JsonProperty("overlayVariable")
  public VariableSpec getOverlayVariable() {
    return this.overlayVariable;
  }

  @JsonProperty("overlayVariable")
  public void setOverlayVariable(VariableSpec overlayVariable) {
    this.overlayVariable = overlayVariable;
  }

  @JsonProperty("facetVariable")
  public List<VariableSpec> getFacetVariable() {
    return this.facetVariable;
  }

  @JsonProperty("facetVariable")
  public void setFacetVariable(List<VariableSpec> facetVariable) {
    this.facetVariable = facetVariable;
  }
}
