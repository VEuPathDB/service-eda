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
    "ecmaScriptExpression",
    "nullResultOnAnyMissingInput",
    "inputVariables",
    "expectedType",
    "expectedShape"
})
public class EcmaScriptExpressionEvalConfigImpl implements EcmaScriptExpressionEvalConfig {
  @JsonProperty("ecmaScriptExpression")
  private String ecmaScriptExpression;

  @JsonProperty("nullResultOnAnyMissingInput")
  private Boolean nullResultOnAnyMissingInput;

  @JsonProperty("inputVariables")
  private List<VariableReference> inputVariables;

  @JsonProperty("expectedType")
  private APIVariableType expectedType;

  @JsonProperty("expectedShape")
  private APIVariableDataShape expectedShape;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("ecmaScriptExpression")
  public String getEcmaScriptExpression() {
    return this.ecmaScriptExpression;
  }

  @JsonProperty("ecmaScriptExpression")
  public void setEcmaScriptExpression(String ecmaScriptExpression) {
    this.ecmaScriptExpression = ecmaScriptExpression;
  }

  @JsonProperty("nullResultOnAnyMissingInput")
  public Boolean getNullResultOnAnyMissingInput() {
    return this.nullResultOnAnyMissingInput;
  }

  @JsonProperty("nullResultOnAnyMissingInput")
  public void setNullResultOnAnyMissingInput(Boolean nullResultOnAnyMissingInput) {
    this.nullResultOnAnyMissingInput = nullResultOnAnyMissingInput;
  }

  @JsonProperty("inputVariables")
  public List<VariableReference> getInputVariables() {
    return this.inputVariables;
  }

  @JsonProperty("inputVariables")
  public void setInputVariables(List<VariableReference> inputVariables) {
    this.inputVariables = inputVariables;
  }

  @JsonProperty("expectedType")
  public APIVariableType getExpectedType() {
    return this.expectedType;
  }

  @JsonProperty("expectedType")
  public void setExpectedType(APIVariableType expectedType) {
    this.expectedType = expectedType;
  }

  @JsonProperty("expectedShape")
  public APIVariableDataShape getExpectedShape() {
    return this.expectedShape;
  }

  @JsonProperty("expectedShape")
  public void setExpectedShape(APIVariableDataShape expectedShape) {
    this.expectedShape = expectedShape;
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
