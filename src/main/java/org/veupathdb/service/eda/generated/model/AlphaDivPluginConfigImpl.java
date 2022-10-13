package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "collectionVariable",
    "alphaDivMethod"
})
public class AlphaDivPluginConfigImpl implements AlphaDivPluginConfig {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("collectionVariable")
  private VariableSpec collectionVariable;

  @JsonProperty("alphaDivMethod")
  private AlphaDivMethod alphaDivMethod;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("collectionVariable")
  public VariableSpec getCollectionVariable() {
    return this.collectionVariable;
  }

  @JsonProperty("collectionVariable")
  public void setCollectionVariable(VariableSpec collectionVariable) {
    this.collectionVariable = collectionVariable;
  }

  @JsonProperty("alphaDivMethod")
  public AlphaDivMethod getAlphaDivMethod() {
    return this.alphaDivMethod;
  }

  @JsonProperty("alphaDivMethod")
  public void setAlphaDivMethod(AlphaDivMethod alphaDivMethod) {
    this.alphaDivMethod = alphaDivMethod;
  }
}
