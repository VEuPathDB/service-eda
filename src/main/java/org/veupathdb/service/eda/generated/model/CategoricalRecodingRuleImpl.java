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
    "inputValues",
    "outputValue"
})
public class CategoricalRecodingRuleImpl implements CategoricalRecodingRule {
  @JsonProperty("inputValues")
  private List<String> inputValues;

  @JsonProperty("outputValue")
  private String outputValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("inputValues")
  public List<String> getInputValues() {
    return this.inputValues;
  }

  @JsonProperty("inputValues")
  public void setInputValues(List<String> inputValues) {
    this.inputValues = inputValues;
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
