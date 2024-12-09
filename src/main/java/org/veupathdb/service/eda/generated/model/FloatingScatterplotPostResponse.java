package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingScatterplotPostResponseImpl.class
)
public interface FloatingScatterplotPostResponse {
  @JsonProperty("scatterplot")
  FloatingScatterplot getScatterplot();

  @JsonProperty("scatterplot")
  void setScatterplot(FloatingScatterplot scatterplot);
}
