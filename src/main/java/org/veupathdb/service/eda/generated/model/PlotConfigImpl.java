package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "completeCasesAllVars",
    "completeCasesAxesVars",
    "variables"
})
public class PlotConfigImpl implements PlotConfig {
  @JsonProperty("completeCasesAllVars")
  private Number completeCasesAllVars;

  @JsonProperty("completeCasesAxesVars")
  private Number completeCasesAxesVars;

  @JsonProperty("variables")
  private List<VariableMapping> variables;

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
}