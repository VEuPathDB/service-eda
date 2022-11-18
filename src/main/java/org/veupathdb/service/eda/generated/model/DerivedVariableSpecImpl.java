package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "variableId",
    "expectedVariableType",
    "expectedVariableDataShape",
    "functionName",
    "inputVars",
    "config"
})
public class DerivedVariableSpecImpl implements DerivedVariableSpec {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("expectedVariableType")
  private APIVariableType expectedVariableType;

  @JsonProperty("expectedVariableDataShape")
  private APIVariableDataShape expectedVariableDataShape;

  @JsonProperty("functionName")
  private String functionName;

  @JsonProperty("inputVars")
  private List<VariableSpec> inputVars;

  @JsonProperty("config")
  private Object config;

  @JsonProperty("entityId")
  public String getEntityId() {
    return this.entityId;
  }

  @JsonProperty("entityId")
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @JsonProperty("variableId")
  public String getVariableId() {
    return this.variableId;
  }

  @JsonProperty("variableId")
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  @JsonProperty("expectedVariableType")
  public APIVariableType getExpectedVariableType() {
    return this.expectedVariableType;
  }

  @JsonProperty("expectedVariableType")
  public void setExpectedVariableType(APIVariableType expectedVariableType) {
    this.expectedVariableType = expectedVariableType;
  }

  @JsonProperty("expectedVariableDataShape")
  public APIVariableDataShape getExpectedVariableDataShape() {
    return this.expectedVariableDataShape;
  }

  @JsonProperty("expectedVariableDataShape")
  public void setExpectedVariableDataShape(APIVariableDataShape expectedVariableDataShape) {
    this.expectedVariableDataShape = expectedVariableDataShape;
  }

  @JsonProperty("functionName")
  public String getFunctionName() {
    return this.functionName;
  }

  @JsonProperty("functionName")
  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  @JsonProperty("inputVars")
  public List<VariableSpec> getInputVars() {
    return this.inputVars;
  }

  @JsonProperty("inputVars")
  public void setInputVars(List<VariableSpec> inputVars) {
    this.inputVars = inputVars;
  }

  @JsonProperty("config")
  public Object getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(Object config) {
    this.config = config;
  }
}
