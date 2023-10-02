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
    "collectionVariable1",
    "collectionVariable2",
    "correlationMethod"
})
public class CorrelationComputeConfigImpl implements CorrelationComputeConfig {
  @JsonProperty("collectionVariable1")
  private VariableSpec collectionVariable1;

  @JsonProperty("collectionVariable2")
  private VariableSpec collectionVariable2;

  @JsonProperty("correlationMethod")
  private CorrelationMethod correlationMethod;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("collectionVariable1")
  public VariableSpec getCollectionVariable1() {
    return this.collectionVariable1;
  }

  @JsonProperty("collectionVariable1")
  public void setCollectionVariable1(VariableSpec collectionVariable1) {
    this.collectionVariable1 = collectionVariable1;
  }

  @JsonProperty("collectionVariable2")
  public VariableSpec getCollectionVariable2() {
    return this.collectionVariable2;
  }

  @JsonProperty("collectionVariable2")
  public void setCollectionVariable2(VariableSpec collectionVariable2) {
    this.collectionVariable2 = collectionVariable2;
  }

  @JsonProperty("correlationMethod")
  public CorrelationMethod getCorrelationMethod() {
    return this.correlationMethod;
  }

  @JsonProperty("correlationMethod")
  public void setCorrelationMethod(CorrelationMethod correlationMethod) {
    this.correlationMethod = correlationMethod;
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
