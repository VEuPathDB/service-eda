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
@JsonTypeName("dateRange")
@JsonPropertyOrder({
    "entityId",
    "type",
    "variableId",
    "min",
    "max"
})
public class APIDateRangeFilterImpl implements APIDateRangeFilter {
  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("type")
  private final APIFilterType type = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("min")
  private String min;

  @JsonProperty("max")
  private String max;

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

  @JsonProperty("min")
  public String getMin() {
    return this.min;
  }

  @JsonProperty("min")
  public void setMin(String min) {
    this.min = min;
  }

  @JsonProperty("max")
  public String getMax() {
    return this.max;
  }

  @JsonProperty("max")
  public void setMax(String max) {
    this.max = max;
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
