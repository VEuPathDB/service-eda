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
public class FloatingLineplotImpl implements FloatingLineplot {
  @JsonProperty("data")
  private List<LineplotData> data;

  @JsonProperty("variables")
  private List<VariableMapping> variables;

  @JsonProperty("data")
  public List<LineplotData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<LineplotData> data) {
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
