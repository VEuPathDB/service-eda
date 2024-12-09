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
    "yAxisNumeratorValues",
    "yAxisDenominatorValues",
    "overlayVariable",
    "facetVariable",
    "binSpec",
    "valueSpec",
    "errorBars",
    "viewport"
})
public class LineplotSpecImpl implements LineplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("showMissingness")
  private ShowMissingnessNoAxes showMissingness;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("yAxisVariable")
  private VariableSpec yAxisVariable;

  @JsonProperty("yAxisNumeratorValues")
  private List<String> yAxisNumeratorValues;

  @JsonProperty("yAxisDenominatorValues")
  private List<String> yAxisDenominatorValues;

  @JsonProperty("overlayVariable")
  private VariableSpec overlayVariable;

  @JsonProperty("facetVariable")
  private List<VariableSpec> facetVariable;

  @JsonProperty("binSpec")
  private BinSpec binSpec;

  @JsonProperty("valueSpec")
  private LineplotSpec.ValueSpecType valueSpec;

  @JsonProperty("errorBars")
  private StringBoolean errorBars;

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

  @JsonProperty("yAxisNumeratorValues")
  public List<String> getYAxisNumeratorValues() {
    return this.yAxisNumeratorValues;
  }

  @JsonProperty("yAxisNumeratorValues")
  public void setYAxisNumeratorValues(List<String> yAxisNumeratorValues) {
    this.yAxisNumeratorValues = yAxisNumeratorValues;
  }

  @JsonProperty("yAxisDenominatorValues")
  public List<String> getYAxisDenominatorValues() {
    return this.yAxisDenominatorValues;
  }

  @JsonProperty("yAxisDenominatorValues")
  public void setYAxisDenominatorValues(List<String> yAxisDenominatorValues) {
    this.yAxisDenominatorValues = yAxisDenominatorValues;
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

  @JsonProperty("binSpec")
  public BinSpec getBinSpec() {
    return this.binSpec;
  }

  @JsonProperty("binSpec")
  public void setBinSpec(BinSpec binSpec) {
    this.binSpec = binSpec;
  }

  @JsonProperty("valueSpec")
  public LineplotSpec.ValueSpecType getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(LineplotSpec.ValueSpecType valueSpec) {
    this.valueSpec = valueSpec;
  }

  @JsonProperty("errorBars")
  public StringBoolean getErrorBars() {
    return this.errorBars;
  }

  @JsonProperty("errorBars")
  public void setErrorBars(StringBoolean errorBars) {
    this.errorBars = errorBars;
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
