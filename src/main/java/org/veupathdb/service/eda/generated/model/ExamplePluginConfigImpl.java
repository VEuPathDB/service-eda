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
    "outputEntityId",
    "inputVariable",
    "valueSuffix"
})
public class ExamplePluginConfigImpl implements ExamplePluginConfig {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("inputVariable")
  private VariableSpec inputVariable;

  @JsonProperty("valueSuffix")
  private String valueSuffix;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("inputVariable")
  public VariableSpec getInputVariable() {
    return this.inputVariable;
  }

  @JsonProperty("inputVariable")
  public void setInputVariable(VariableSpec inputVariable) {
    this.inputVariable = inputVariable;
  }

  @JsonProperty("valueSuffix")
  public String getValueSuffix() {
    return this.valueSuffix;
  }

  @JsonProperty("valueSuffix")
  public void setValueSuffix(String valueSuffix) {
    this.valueSuffix = valueSuffix;
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
