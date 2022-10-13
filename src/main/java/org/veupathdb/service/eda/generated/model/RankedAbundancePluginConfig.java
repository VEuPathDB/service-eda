package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = RankedAbundancePluginConfigImpl.class
)
public interface RankedAbundancePluginConfig {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("collectionVariable")
  VariableSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(VariableSpec collectionVariable);

  @JsonProperty("rankingMethod")
  RankingMethod getRankingMethod();

  @JsonProperty("rankingMethod")
  void setRankingMethod(RankingMethod rankingMethod);
}
