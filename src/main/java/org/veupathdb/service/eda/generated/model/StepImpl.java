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
    "key",
    "operation",
    "leftStepKey",
    "leftVariable",
    "leftVariableTrueValues",
    "rightStepKey",
    "rightVariable",
    "rightVariableTrueValues"
})
public class StepImpl implements Step {
  @JsonProperty("key")
  private String key;

  @JsonProperty("operation")
  private SetOperation operation;

  @JsonProperty("leftStepKey")
  private String leftStepKey;

  @JsonProperty("leftVariable")
  private VariableSpec leftVariable;

  @JsonProperty("leftVariableTrueValues")
  private List<String> leftVariableTrueValues;

  @JsonProperty("rightStepKey")
  private String rightStepKey;

  @JsonProperty("rightVariable")
  private VariableSpec rightVariable;

  @JsonProperty("rightVariableTrueValues")
  private List<String> rightVariableTrueValues;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("key")
  public String getKey() {
    return this.key;
  }

  @JsonProperty("key")
  public void setKey(String key) {
    this.key = key;
  }

  @JsonProperty("operation")
  public SetOperation getOperation() {
    return this.operation;
  }

  @JsonProperty("operation")
  public void setOperation(SetOperation operation) {
    this.operation = operation;
  }

  @JsonProperty("leftStepKey")
  public String getLeftStepKey() {
    return this.leftStepKey;
  }

  @JsonProperty("leftStepKey")
  public void setLeftStepKey(String leftStepKey) {
    this.leftStepKey = leftStepKey;
  }

  @JsonProperty("leftVariable")
  public VariableSpec getLeftVariable() {
    return this.leftVariable;
  }

  @JsonProperty("leftVariable")
  public void setLeftVariable(VariableSpec leftVariable) {
    this.leftVariable = leftVariable;
  }

  @JsonProperty("leftVariableTrueValues")
  public List<String> getLeftVariableTrueValues() {
    return this.leftVariableTrueValues;
  }

  @JsonProperty("leftVariableTrueValues")
  public void setLeftVariableTrueValues(List<String> leftVariableTrueValues) {
    this.leftVariableTrueValues = leftVariableTrueValues;
  }

  @JsonProperty("rightStepKey")
  public String getRightStepKey() {
    return this.rightStepKey;
  }

  @JsonProperty("rightStepKey")
  public void setRightStepKey(String rightStepKey) {
    this.rightStepKey = rightStepKey;
  }

  @JsonProperty("rightVariable")
  public VariableSpec getRightVariable() {
    return this.rightVariable;
  }

  @JsonProperty("rightVariable")
  public void setRightVariable(VariableSpec rightVariable) {
    this.rightVariable = rightVariable;
  }

  @JsonProperty("rightVariableTrueValues")
  public List<String> getRightVariableTrueValues() {
    return this.rightVariableTrueValues;
  }

  @JsonProperty("rightVariableTrueValues")
  public void setRightVariableTrueValues(List<String> rightVariableTrueValues) {
    this.rightVariableTrueValues = rightVariableTrueValues;
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
