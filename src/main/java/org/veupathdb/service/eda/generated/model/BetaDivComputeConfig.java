package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = BetaDivComputeConfigImpl.class
)
public interface BetaDivComputeConfig {
  @JsonProperty("collectionVariable")
  VariableSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(VariableSpec collectionVariable);

  @JsonProperty("betaDivDissimilarityMethod")
  BetaDivDissimilarityMethod getBetaDivDissimilarityMethod();

  @JsonProperty("betaDivDissimilarityMethod")
  void setBetaDivDissimilarityMethod(BetaDivDissimilarityMethod betaDivDissimilarityMethod);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
