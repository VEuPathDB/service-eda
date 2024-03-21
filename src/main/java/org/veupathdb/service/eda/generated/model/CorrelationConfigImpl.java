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
    "correlationMethod",
    "prefilterThresholds",
    "collectionVariable",
    "collectionVariable2"
})
public class CorrelationConfigImpl implements CorrelationConfig {
  @JsonProperty("correlationMethod")
  private CorrelationMethod correlationMethod;

  @JsonProperty("prefilterThresholds")
  private FeaturePrefilterThresholds prefilterThresholds;

  @JsonProperty("collectionVariable")
  private CollectionSpec collectionVariable;

  @JsonProperty("collectionVariable2")
  private CollectionSpec collectionVariable2;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("correlationMethod")
  public CorrelationMethod getCorrelationMethod() {
    return this.correlationMethod;
  }

  @JsonProperty("correlationMethod")
  public void setCorrelationMethod(CorrelationMethod correlationMethod) {
    this.correlationMethod = correlationMethod;
  }

  @JsonProperty("prefilterThresholds")
  public FeaturePrefilterThresholds getPrefilterThresholds() {
    return this.prefilterThresholds;
  }

  @JsonProperty("prefilterThresholds")
  public void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds) {
    this.prefilterThresholds = prefilterThresholds;
  }

  @JsonProperty("collectionVariable")
  public CollectionSpec getCollectionVariable() {
    return this.collectionVariable;
  }

  @JsonProperty("collectionVariable")
  public void setCollectionVariable(CollectionSpec collectionVariable) {
    this.collectionVariable = collectionVariable;
  }

  @JsonProperty("collectionVariable2")
  public CollectionSpec getCollectionVariable2() {
    return this.collectionVariable2;
  }

  @JsonProperty("collectionVariable2")
  public void setCollectionVariable2(CollectionSpec collectionVariable2) {
    this.collectionVariable2 = collectionVariable2;
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
