package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "inputVariable",
    "rules",
    "unmappedValue"
})
public class ContinuousNumericRecodingConfigImpl implements ContinuousNumericRecodingConfig {
  @JsonProperty("inputVariable")
  private VariableSpec inputVariable;

  @JsonProperty("rules")
  private List<ContinuousNumericRule> rules;

  @JsonProperty("unmappedValue")
  private String unmappedValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("inputVariable")
  public VariableSpec getInputVariable() {
    return this.inputVariable;
  }

  @JsonProperty("inputVariable")
  public void setInputVariable(VariableSpec inputVariable) {
    this.inputVariable = inputVariable;
  }

  @JsonProperty("rules")
  public List<ContinuousNumericRule> getRules() {
    return this.rules;
  }

  @JsonProperty("rules")
  public void setRules(List<ContinuousNumericRule> rules) {
    this.rules = rules;
  }

  @JsonProperty("unmappedValue")
  public String getUnmappedValue() {
    return this.unmappedValue;
  }

  @JsonProperty("unmappedValue")
  public void setUnmappedValue(String unmappedValue) {
    this.unmappedValue = unmappedValue;
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
