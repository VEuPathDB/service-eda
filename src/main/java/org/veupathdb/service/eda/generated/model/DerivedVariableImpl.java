package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "variableId",
    "variableType",
    "variableDataShape",
    "derivationType",
    "functionName",
    "inputVars",
    "config"
})
public class DerivedVariableImpl implements DerivedVariable {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("variableType")
  private APIVariableType variableType;

  @JsonProperty("variableDataShape")
  private APIVariableDataShape variableDataShape;

  @JsonProperty("derivationType")
  private DerivationType derivationType;

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

  @JsonProperty("variableType")
  public APIVariableType getVariableType() {
    return this.variableType;
  }

  @JsonProperty("variableType")
  public void setVariableType(APIVariableType variableType) {
    this.variableType = variableType;
  }

  @JsonProperty("variableDataShape")
  public APIVariableDataShape getVariableDataShape() {
    return this.variableDataShape;
  }

  @JsonProperty("variableDataShape")
  public void setVariableDataShape(APIVariableDataShape variableDataShape) {
    this.variableDataShape = variableDataShape;
  }

  @JsonProperty("derivationType")
  public DerivationType getDerivationType() {
    return this.derivationType;
  }

  @JsonProperty("derivationType")
  public void setDerivationType(DerivationType derivationType) {
    this.derivationType = derivationType;
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
