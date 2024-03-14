package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = EntityCountPostResponseImpl.class
)
public interface EntityCountPostResponse {
  @JsonProperty("count")
  Long getCount();

  @JsonProperty("count")
  void setCount(Long count);
}
