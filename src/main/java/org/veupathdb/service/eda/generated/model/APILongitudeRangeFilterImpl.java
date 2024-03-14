package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("longitudeRange")
@JsonPropertyOrder({
    "entityId",
    "type",
    "variableId",
    "left",
    "right"
})
public class APILongitudeRangeFilterImpl implements APILongitudeRangeFilter {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("type")
  private final APIFilterType type = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("left")
  private Number left;

  @JsonProperty("right")
  private Number right;

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

  @JsonProperty("variableId")
  public String getVariableId() {
    return this.variableId;
  }

  @JsonProperty("variableId")
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  @JsonProperty("left")
  public Number getLeft() {
    return this.left;
  }

  @JsonProperty("left")
  public void setLeft(Number left) {
    this.left = left;
  }

  @JsonProperty("right")
  public Number getRight() {
    return this.right;
  }

  @JsonProperty("right")
  public void setRight(Number right) {
    this.right = right;
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
