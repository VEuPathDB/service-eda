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
    "zAxisVariable",
    "facetVariable"
})
public class HeatmapSpecImpl implements HeatmapSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("showMissingness")
  private ShowMissingnessNoAxes showMissingness;

  @JsonProperty("valueSpec")
  private HeatmapSpec.ValueSpecType valueSpec;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("yAxisVariable")
  private VariableSpec yAxisVariable;

  @JsonProperty("zAxisVariable")
  private VariableSpec zAxisVariable;

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

  @JsonProperty("valueSpec")
  public HeatmapSpec.ValueSpecType getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(HeatmapSpec.ValueSpecType valueSpec) {
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

  @JsonProperty("zAxisVariable")
  public VariableSpec getZAxisVariable() {
    return this.zAxisVariable;
  }

  @JsonProperty("zAxisVariable")
  public void setZAxisVariable(VariableSpec zAxisVariable) {
    this.zAxisVariable = zAxisVariable;
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
