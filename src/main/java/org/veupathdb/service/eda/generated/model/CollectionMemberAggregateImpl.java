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
    "variableId",
    "value",
    "confidenceInterval",
    "n"
})
public class CollectionMemberAggregateImpl implements CollectionMemberAggregate {
  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("value")
  private Number value;

  @JsonProperty("confidenceInterval")
  private NumberRange confidenceInterval;

  @JsonProperty("n")
  private Number n;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("variableId")
  public String getVariableId() {
    return this.variableId;
  }

  @JsonProperty("variableId")
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  @JsonProperty("value")
  public Number getValue() {
    return this.value;
  }

  @JsonProperty("value")
  public void setValue(Number value) {
    this.value = value;
  }

  @JsonProperty("confidenceInterval")
  public NumberRange getConfidenceInterval() {
    return this.confidenceInterval;
  }

  @JsonProperty("confidenceInterval")
  public void setConfidenceInterval(NumberRange confidenceInterval) {
    this.confidenceInterval = confidenceInterval;
  }

  @JsonProperty("n")
  public Number getN() {
    return this.n;
  }

  @JsonProperty("n")
  public void setN(Number n) {
    this.n = n;
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
