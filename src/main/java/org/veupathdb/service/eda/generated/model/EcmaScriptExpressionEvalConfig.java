package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = EcmaScriptExpressionEvalConfigImpl.class
)
public interface EcmaScriptExpressionEvalConfig {
  @JsonProperty("ecmaScriptExpression")
  String getEcmaScriptExpression();

  @JsonProperty("ecmaScriptExpression")
  void setEcmaScriptExpression(String ecmaScriptExpression);

  @JsonProperty("nullResultOnAnyMissingInput")
  Boolean getNullResultOnAnyMissingInput();

  @JsonProperty("nullResultOnAnyMissingInput")
  void setNullResultOnAnyMissingInput(Boolean nullResultOnAnyMissingInput);

  @JsonProperty("inputVariables")
  List<VariableReference> getInputVariables();

  @JsonProperty("inputVariables")
  void setInputVariables(List<VariableReference> inputVariables);

  @JsonProperty("expectedType")
  APIVariableType getExpectedType();

  @JsonProperty("expectedType")
  void setExpectedType(APIVariableType expectedType);

  @JsonProperty("expectedShape")
  APIVariableDataShape getExpectedShape();

  @JsonProperty("expectedShape")
  void setExpectedShape(APIVariableDataShape expectedShape);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
