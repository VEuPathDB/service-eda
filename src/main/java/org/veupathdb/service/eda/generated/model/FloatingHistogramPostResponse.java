package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingHistogramPostResponseImpl.class
)
public interface FloatingHistogramPostResponse {
  @JsonProperty("histogram")
  FloatingHistogram getHistogram();

  @JsonProperty("histogram")
  void setHistogram(FloatingHistogram histogram);
}
