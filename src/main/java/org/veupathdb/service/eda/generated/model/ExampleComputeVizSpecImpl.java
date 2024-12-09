package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("prefixVar")
public class ExampleComputeVizSpecImpl implements ExampleComputeVizSpec {
  @JsonProperty("prefixVar")
  private VariableSpec prefixVar;

  @JsonProperty("prefixVar")
  public VariableSpec getPrefixVar() {
    return this.prefixVar;
  }

  @JsonProperty("prefixVar")
  public void setPrefixVar(VariableSpec prefixVar) {
    this.prefixVar = prefixVar;
  }
}
