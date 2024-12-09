package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "datasetId",
    "entityId",
    "displayName",
    "functionName",
    "config",
    "description"
})
public class DerivedVariablePostRequestImpl implements DerivedVariablePostRequest {
  @JsonProperty("datasetId")
  private String datasetId;

  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("functionName")
  private String functionName;

  @JsonProperty("config")
  private Object config;

  @JsonProperty("description")
  private String description;

  @JsonProperty("datasetId")
  public String getDatasetId() {
    return this.datasetId;
  }

  @JsonProperty("datasetId")
  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  @JsonProperty("entityId")
  public String getEntityId() {
    return this.entityId;
  }

  @JsonProperty("entityId")
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("functionName")
  public String getFunctionName() {
    return this.functionName;
  }

  @JsonProperty("functionName")
  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  @JsonProperty("config")
  public Object getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(Object config) {
    this.config = config;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }
}
