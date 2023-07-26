package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("numJobsExpired")
public class ExpiredJobsResponseImpl implements ExpiredJobsResponse {
  @JsonProperty("numJobsExpired")
  private Integer numJobsExpired;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("numJobsExpired")
  public Integer getNumJobsExpired() {
    return this.numJobsExpired;
  }

  @JsonProperty("numJobsExpired")
  public void setNumJobsExpired(Integer numJobsExpired) {
    this.numJobsExpired = numJobsExpired;
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
