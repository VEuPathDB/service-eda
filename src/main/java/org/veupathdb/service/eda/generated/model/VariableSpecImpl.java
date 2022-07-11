package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "entityId",
    "variableId"
})
public class VariableSpecImpl implements VariableSpec {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("variableId")
  private String variableId;

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
}
