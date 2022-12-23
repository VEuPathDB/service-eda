package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = RankedAbundanceComputeConfigImpl.class
)
public interface RankedAbundanceComputeConfig {
  @JsonProperty("collectionVariable")
  VariableSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(VariableSpec collectionVariable);

  @JsonProperty("rankingMethod")
  RankingMethod getRankingMethod();

  @JsonProperty("rankingMethod")
  void setRankingMethod(RankingMethod rankingMethod);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
