package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("numEmptyValues")
public class ExamplePluginStatsImpl implements ExamplePluginStats {
  @JsonProperty("numEmptyValues")
  private Integer numEmptyValues;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("numEmptyValues")
  public Integer getNumEmptyValues() {
    return this.numEmptyValues;
  }

  @JsonProperty("numEmptyValues")
  public void setNumEmptyValues(Integer numEmptyValues) {
    this.numEmptyValues = numEmptyValues;
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
