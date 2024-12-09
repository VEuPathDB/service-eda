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
    "minInclusive",
    "maxExclusive",
    "outputValue"
})
public class ContinuousNumericRuleImpl implements ContinuousNumericRule {
  @JsonProperty("minInclusive")
  private Number minInclusive;

  @JsonProperty("maxExclusive")
  private Number maxExclusive;

  @JsonProperty("outputValue")
  private String outputValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("minInclusive")
  public Number getMinInclusive() {
    return this.minInclusive;
  }

  @JsonProperty("minInclusive")
  public void setMinInclusive(Number minInclusive) {
    this.minInclusive = minInclusive;
  }

  @JsonProperty("maxExclusive")
  public Number getMaxExclusive() {
    return this.maxExclusive;
  }

  @JsonProperty("maxExclusive")
  public void setMaxExclusive(Number maxExclusive) {
    this.maxExclusive = maxExclusive;
  }

  @JsonProperty("outputValue")
  public String getOutputValue() {
    return this.outputValue;
  }

  @JsonProperty("outputValue")
  public void setOutputValue(String outputValue) {
    this.outputValue = outputValue;
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
