package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "xAxisVariable",
    "yAxisVariable",
    "yAxisNumeratorValues",
    "yAxisDenominatorValues",
    "overlayConfig",
    "binSpec",
    "valueSpec",
    "errorBars",
    "viewport"
})
public class FloatingLineplotSpecImpl implements FloatingLineplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("yAxisVariable")
  private VariableSpec yAxisVariable;

  @JsonProperty("yAxisNumeratorValues")
  private List<String> yAxisNumeratorValues;

  @JsonProperty("yAxisDenominatorValues")
  private List<String> yAxisDenominatorValues;

  @JsonProperty("overlayConfig")
  private OverlayConfig overlayConfig;

  @JsonProperty("binSpec")
  private BinSpec binSpec;

  @JsonProperty("valueSpec")
  private FloatingLineplotSpec.ValueSpecType valueSpec;

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

  @JsonProperty("overlayConfig")
  public OverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(OverlayConfig overlayConfig) {
    this.overlayConfig = overlayConfig;
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
  public FloatingLineplotSpec.ValueSpecType getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(FloatingLineplotSpec.ValueSpecType valueSpec) {
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
