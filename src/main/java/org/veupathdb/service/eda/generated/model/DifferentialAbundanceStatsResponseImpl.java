package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("someStats")
public class DifferentialAbundanceStatsResponseImpl implements DifferentialAbundanceStatsResponse {
  @JsonProperty("someStats")
  private Object someStats;

  @JsonIgnore
  private Map<String, java.lang.Object> additionalProperties = new ExcludingMap();

  @JsonProperty("someStats")
  public Object getSomeStats() {
    return this.someStats;
  }

  @JsonProperty("someStats")
  public void setSomeStats(Object someStats) {
    this.someStats = someStats;
  }

  @JsonAnyGetter
  public Map<String, java.lang.Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, java.lang.Object value) {
    this.additionalProperties.put(key, value);
  }
}
