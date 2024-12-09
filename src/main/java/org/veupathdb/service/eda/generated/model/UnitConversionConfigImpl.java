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
    "inputVariable",
    "outputUnits"
})
public class UnitConversionConfigImpl implements UnitConversionConfig {
  @JsonProperty("inputVariable")
  private VariableSpec inputVariable;

  @JsonProperty("outputUnits")
  private String outputUnits;

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

  @JsonProperty("outputUnits")
  public String getOutputUnits() {
    return this.outputUnits;
  }

  @JsonProperty("outputUnits")
  public void setOutputUnits(String outputUnits) {
    this.outputUnits = outputUnits;
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
