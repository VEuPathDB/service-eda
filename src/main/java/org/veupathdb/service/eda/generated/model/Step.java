package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = StepImpl.class
)
public interface Step {
  @JsonProperty("key")
  String getKey();

  @JsonProperty("key")
  void setKey(String key);

  @JsonProperty("operation")
  SetOperation getOperation();

  @JsonProperty("operation")
  void setOperation(SetOperation operation);

  @JsonProperty("leftStepKey")
  String getLeftStepKey();

  @JsonProperty("leftStepKey")
  void setLeftStepKey(String leftStepKey);

  @JsonProperty("leftVariable")
  VariableSpec getLeftVariable();

  @JsonProperty("leftVariable")
  void setLeftVariable(VariableSpec leftVariable);

  @JsonProperty("leftVariableTrueValues")
  List<String> getLeftVariableTrueValues();

  @JsonProperty("leftVariableTrueValues")
  void setLeftVariableTrueValues(List<String> leftVariableTrueValues);

  @JsonProperty("rightStepKey")
  String getRightStepKey();

  @JsonProperty("rightStepKey")
  void setRightStepKey(String rightStepKey);

  @JsonProperty("rightVariable")
  VariableSpec getRightVariable();

  @JsonProperty("rightVariable")
  void setRightVariable(VariableSpec rightVariable);

  @JsonProperty("rightVariableTrueValues")
  List<String> getRightVariableTrueValues();

  @JsonProperty("rightVariableTrueValues")
  void setRightVariableTrueValues(List<String> rightVariableTrueValues);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
