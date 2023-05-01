package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = ExpiredJobsResponseImpl.class
)
public interface ExpiredJobsResponse {
  @JsonProperty("numJobsExpired")
  Integer getNumJobsExpired();

  @JsonProperty("numJobsExpired")
  void setNumJobsExpired(Integer numJobsExpired);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
