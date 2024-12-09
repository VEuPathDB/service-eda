package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "showMissingness",
    "valueSpec",
    "xAxisVariable",
    "yAxisVariable",
    "overlayVariable",
    "facetVariable",
    "maxAllowedDataPoints",
    "correlationMethod"
})
public class ScatterplotSpecImpl implements ScatterplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("showMissingness")
  private ShowMissingnessNoAxes showMissingness;

  @JsonProperty("valueSpec")
  private ScatterplotSpec.ValueSpecType valueSpec;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("yAxisVariable")
  private VariableSpec yAxisVariable;

  @JsonProperty("overlayVariable")
  private VariableSpec overlayVariable;

  @JsonProperty("facetVariable")
  private List<VariableSpec> facetVariable;

  @JsonProperty("maxAllowedDataPoints")
  private Long maxAllowedDataPoints;

  @JsonProperty("correlationMethod")
  private ScatterCorrelationMethod correlationMethod;

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

  @JsonProperty("valueSpec")
  public ScatterplotSpec.ValueSpecType getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(ScatterplotSpec.ValueSpecType valueSpec) {
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

  @JsonProperty("yAxisVariable")
  public VariableSpec getYAxisVariable() {
    return this.yAxisVariable;
  }

  @JsonProperty("yAxisVariable")
  public void setYAxisVariable(VariableSpec yAxisVariable) {
    this.yAxisVariable = yAxisVariable;
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

  @JsonProperty("maxAllowedDataPoints")
  public Long getMaxAllowedDataPoints() {
    return this.maxAllowedDataPoints;
  }

  @JsonProperty("maxAllowedDataPoints")
  public void setMaxAllowedDataPoints(Long maxAllowedDataPoints) {
    this.maxAllowedDataPoints = maxAllowedDataPoints;
  }

  @JsonProperty("correlationMethod")
  public ScatterCorrelationMethod getCorrelationMethod() {
    return this.correlationMethod;
  }

  @JsonProperty("correlationMethod")
  public void setCorrelationMethod(ScatterCorrelationMethod correlationMethod) {
    this.correlationMethod = correlationMethod;
  }
}
