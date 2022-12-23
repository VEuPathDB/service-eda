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
    "betaDivDistanceMethod"
})
public class BetaDivComputeConfigImpl implements BetaDivComputeConfig {
  @JsonProperty("collectionVariable")
  private VariableSpec collectionVariable;

  @JsonProperty("betaDivDistanceMethod")
  private BetaDivDistanceMethod betaDivDistanceMethod;

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

  @JsonProperty("betaDivDistanceMethod")
  public BetaDivDistanceMethod getBetaDivDistanceMethod() {
    return this.betaDivDistanceMethod;
  }

  @JsonProperty("betaDivDistanceMethod")
  public void setBetaDivDistanceMethod(BetaDivDistanceMethod betaDivDistanceMethod) {
    this.betaDivDistanceMethod = betaDivDistanceMethod;
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
