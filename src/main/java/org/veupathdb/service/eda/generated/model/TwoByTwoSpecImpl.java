package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "showMissingness",
    "xAxisVariable",
    "yAxisVariable",
    "facetVariable",
    "xAxisReferenceValue",
    "yAxisReferenceValue"
})
public class TwoByTwoSpecImpl implements TwoByTwoSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("showMissingness")
  private ShowMissingnessNoAxes showMissingness;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("yAxisVariable")
  private VariableSpec yAxisVariable;

  @JsonProperty("facetVariable")
  private List<VariableSpec> facetVariable;

  @JsonProperty("xAxisReferenceValue")
  private String xAxisReferenceValue;

  @JsonProperty("yAxisReferenceValue")
  private String yAxisReferenceValue;

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

  @JsonProperty("facetVariable")
  public List<VariableSpec> getFacetVariable() {
    return this.facetVariable;
  }

  @JsonProperty("facetVariable")
  public void setFacetVariable(List<VariableSpec> facetVariable) {
    this.facetVariable = facetVariable;
  }

  @JsonProperty("xAxisReferenceValue")
  public String getXAxisReferenceValue() {
    return this.xAxisReferenceValue;
  }

  @JsonProperty("xAxisReferenceValue")
  public void setXAxisReferenceValue(String xAxisReferenceValue) {
    this.xAxisReferenceValue = xAxisReferenceValue;
  }

  @JsonProperty("yAxisReferenceValue")
  public String getYAxisReferenceValue() {
    return this.yAxisReferenceValue;
  }

  @JsonProperty("yAxisReferenceValue")
  public void setYAxisReferenceValue(String yAxisReferenceValue) {
    this.yAxisReferenceValue = yAxisReferenceValue;
  }
}
