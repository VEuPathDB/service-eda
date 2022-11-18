package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DerivedVariableSpecImpl.class
)
public interface DerivedVariableSpec extends VariableSpec {
  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("expectedVariableType")
  APIVariableType getExpectedVariableType();

  @JsonProperty("expectedVariableType")
  void setExpectedVariableType(APIVariableType expectedVariableType);

  @JsonProperty("expectedVariableDataShape")
  APIVariableDataShape getExpectedVariableDataShape();

  @JsonProperty("expectedVariableDataShape")
  void setExpectedVariableDataShape(APIVariableDataShape expectedVariableDataShape);

  @JsonProperty("functionName")
  String getFunctionName();

  @JsonProperty("functionName")
  void setFunctionName(String functionName);

  @JsonProperty("inputVars")
  List<VariableSpec> getInputVars();

  @JsonProperty("inputVars")
  void setInputVars(List<VariableSpec> inputVars);

  @JsonProperty("config")
  Object getConfig();

  @JsonProperty("config")
  void setConfig(Object config);
}
