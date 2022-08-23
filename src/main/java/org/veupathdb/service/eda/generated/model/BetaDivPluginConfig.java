package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = BetaDivPluginConfigImpl.class
)
public interface BetaDivPluginConfig {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("collectionVariable")
  VariableSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(VariableSpec collectionVariable);

  @JsonProperty("betaDivDistanceMethod")
  BetaDivDistanceMethod getBetaDivDistanceMethod();

  @JsonProperty("betaDivDistanceMethod")
  void setBetaDivDistanceMethod(BetaDivDistanceMethod betaDivDistanceMethod);
}
