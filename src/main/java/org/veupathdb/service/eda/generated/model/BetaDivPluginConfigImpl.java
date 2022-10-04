package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "collectionVariable",
    "betaDivDistanceMethod"
})
public class BetaDivPluginConfigImpl implements BetaDivPluginConfig {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("collectionVariable")
  private VariableSpec collectionVariable;

  @JsonProperty("betaDivDistanceMethod")
  private BetaDivDistanceMethod betaDivDistanceMethod;

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

  @JsonProperty("betaDivDistanceMethod")
  public BetaDivDistanceMethod getBetaDivDistanceMethod() {
    return this.betaDivDistanceMethod;
  }

  @JsonProperty("betaDivDistanceMethod")
  public void setBetaDivDistanceMethod(BetaDivDistanceMethod betaDivDistanceMethod) {
    this.betaDivDistanceMethod = betaDivDistanceMethod;
  }
}
