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
    "heightVariable",
    "weightVariable"
})
public class BodyMassIndexConfigImpl implements BodyMassIndexConfig {
  @JsonProperty("heightVariable")
  private VariableSpec heightVariable;

  @JsonProperty("weightVariable")
  private VariableSpec weightVariable;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("heightVariable")
  public VariableSpec getHeightVariable() {
    return this.heightVariable;
  }

  @JsonProperty("heightVariable")
  public void setHeightVariable(VariableSpec heightVariable) {
    this.heightVariable = heightVariable;
  }

  @JsonProperty("weightVariable")
  public VariableSpec getWeightVariable() {
    return this.weightVariable;
  }

  @JsonProperty("weightVariable")
  public void setWeightVariable(VariableSpec weightVariable) {
    this.weightVariable = weightVariable;
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
