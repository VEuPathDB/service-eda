package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "completeCasesAllVars",
    "completeCasesAxesVars",
    "variables",
    "binSlider",
    "binSpec",
    "viewport"
})
public class LineplotConfigImpl implements LineplotConfig {
  @JsonProperty("completeCasesAllVars")
  private Number completeCasesAllVars;

  @JsonProperty("completeCasesAxesVars")
  private Number completeCasesAxesVars;

  @JsonProperty("variables")
  private List<VariableMapping> variables;

  @JsonProperty("binSlider")
  private BinSlider binSlider;

  @JsonProperty("binSpec")
  private BinSpec binSpec;

  @JsonProperty("viewport")
  private NumericViewport viewport;

  @JsonProperty("completeCasesAllVars")
  public Number getCompleteCasesAllVars() {
    return this.completeCasesAllVars;
  }

  @JsonProperty("completeCasesAllVars")
  public void setCompleteCasesAllVars(Number completeCasesAllVars) {
    this.completeCasesAllVars = completeCasesAllVars;
  }

  @JsonProperty("completeCasesAxesVars")
  public Number getCompleteCasesAxesVars() {
    return this.completeCasesAxesVars;
  }

  @JsonProperty("completeCasesAxesVars")
  public void setCompleteCasesAxesVars(Number completeCasesAxesVars) {
    this.completeCasesAxesVars = completeCasesAxesVars;
  }

  @JsonProperty("variables")
  public List<VariableMapping> getVariables() {
    return this.variables;
  }

  @JsonProperty("variables")
  public void setVariables(List<VariableMapping> variables) {
    this.variables = variables;
  }

  @JsonProperty("binSlider")
  public BinSlider getBinSlider() {
    return this.binSlider;
  }

  @JsonProperty("binSlider")
  public void setBinSlider(BinSlider binSlider) {
    this.binSlider = binSlider;
  }

  @JsonProperty("binSpec")
  public BinSpec getBinSpec() {
    return this.binSpec;
  }

  @JsonProperty("binSpec")
  public void setBinSpec(BinSpec binSpec) {
    this.binSpec = binSpec;
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
