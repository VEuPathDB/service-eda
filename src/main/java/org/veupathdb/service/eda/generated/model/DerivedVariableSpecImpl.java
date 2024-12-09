package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "variableId",
    "functionName",
    "displayName",
    "config"
})
public class DerivedVariableSpecImpl implements DerivedVariableSpec {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("functionName")
  private String functionName;

  @JsonProperty("displayName")
  private String displayName;

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

  @JsonProperty("functionName")
  public String getFunctionName() {
    return this.functionName;
  }

  @JsonProperty("functionName")
  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
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
