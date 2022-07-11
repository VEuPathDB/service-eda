package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DerivedVariableImpl.class
)
public interface DerivedVariable extends VariableSpec {
  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("variableType")
  APIVariableType getVariableType();

  @JsonProperty("variableType")
  void setVariableType(APIVariableType variableType);

  @JsonProperty("variableDataShape")
  APIVariableDataShape getVariableDataShape();

  @JsonProperty("variableDataShape")
  void setVariableDataShape(APIVariableDataShape variableDataShape);

  @JsonProperty("derivationType")
  DerivationType getDerivationType();

  @JsonProperty("derivationType")
  void setDerivationType(DerivationType derivationType);

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
