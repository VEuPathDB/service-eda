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
    "imputeZero"
})
public class SingleNumericVarReductionConfigImpl implements SingleNumericVarReductionConfig {
  @JsonProperty("inputVariable")
  private VariableSpec inputVariable;

  @JsonProperty("imputeZero")
  private Boolean imputeZero;

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

  @JsonProperty("imputeZero")
  public Boolean getImputeZero() {
    return this.imputeZero;
  }

  @JsonProperty("imputeZero")
  public void setImputeZero(Boolean imputeZero) {
    this.imputeZero = imputeZero;
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
