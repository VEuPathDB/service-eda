package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "variable",
    "metadata"
})
public class ContinuousVariableMetadataSpecImpl implements ContinuousVariableMetadataSpec {
  @JsonProperty("variable")
  private VariableSpec variable;

  @JsonProperty("metadata")
  private List<String> metadata;

  @JsonProperty("variable")
  public VariableSpec getVariable() {
    return this.variable;
  }

  @JsonProperty("variable")
  public void setVariable(VariableSpec variable) {
    this.variable = variable;
  }

  @JsonProperty("metadata")
  public List<String> getMetadata() {
    return this.metadata;
  }

  @JsonProperty("metadata")
  public void setMetadata(List<String> metadata) {
    this.metadata = metadata;
  }
}
