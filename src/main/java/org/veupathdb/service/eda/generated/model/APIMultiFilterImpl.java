package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("multiFilter")
@JsonPropertyOrder({
    "entityId",
    "type",
    "operation",
    "variableId",
    "subFilters"
})
public class APIMultiFilterImpl implements APIMultiFilter {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("type")
  private final APIFilterType type = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("operation")
  private APIBooleanOperationType operation;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("subFilters")
  private List<APIMultiFilterSubFilter> subFilters;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("entityId")
  public String getEntityId() {
    return this.entityId;
  }

  @JsonProperty("entityId")
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @JsonProperty("type")
  public APIFilterType getType() {
    return this.type;
  }

  @JsonProperty("operation")
  public APIBooleanOperationType getOperation() {
    return this.operation;
  }

  @JsonProperty("operation")
  public void setOperation(APIBooleanOperationType operation) {
    this.operation = operation;
  }

  @JsonProperty("variableId")
  public String getVariableId() {
    return this.variableId;
  }

  @JsonProperty("variableId")
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  @JsonProperty("subFilters")
  public List<APIMultiFilterSubFilter> getSubFilters() {
    return this.subFilters;
  }

  @JsonProperty("subFilters")
  public void setSubFilters(List<APIMultiFilterSubFilter> subFilters) {
    this.subFilters = subFilters;
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
