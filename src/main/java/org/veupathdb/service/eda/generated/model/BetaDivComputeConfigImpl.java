package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "collectionVariable",
    "betaDivDissimilarityMethod"
})
public class BetaDivComputeConfigImpl implements BetaDivComputeConfig {
  @JsonProperty("collectionVariable")
  private VariableSpec collectionVariable;

  @JsonProperty("betaDivDissimilarityMethod")
  private BetaDivDissimilarityMethod betaDivDissimilarityMethod;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("collectionVariable")
  public VariableSpec getCollectionVariable() {
    return this.collectionVariable;
  }

  @JsonProperty("collectionVariable")
  public void setCollectionVariable(VariableSpec collectionVariable) {
    this.collectionVariable = collectionVariable;
  }

  @JsonProperty("betaDivDissimilarityMethod")
  public BetaDivDissimilarityMethod getBetaDivDissimilarityMethod() {
    return this.betaDivDissimilarityMethod;
  }

  @JsonProperty("betaDivDissimilarityMethod")
  public void setBetaDivDissimilarityMethod(BetaDivDissimilarityMethod betaDivDissimilarityMethod) {
    this.betaDivDissimilarityMethod = betaDivDissimilarityMethod;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
