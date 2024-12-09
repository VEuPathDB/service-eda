package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingBarplotPostResponseImpl.class
)
public interface FloatingBarplotPostResponse {
  @JsonProperty("barplot")
  FloatingBarplot getBarplot();

  @JsonProperty("barplot")
  void setBarplot(FloatingBarplot barplot);
}
