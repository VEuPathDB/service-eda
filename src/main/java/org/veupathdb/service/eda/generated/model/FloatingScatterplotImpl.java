package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "variables"
})
public class FloatingScatterplotImpl implements FloatingScatterplot {
  @JsonProperty("data")
  private List<ScatterplotData> data;

  @JsonProperty("variables")
  private List<VariableMapping> variables;

  @JsonProperty("data")
  public List<ScatterplotData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<ScatterplotData> data) {
    this.data = data;
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