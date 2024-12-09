package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingBoxplotPostResponseImpl.class
)
public interface FloatingBoxplotPostResponse {
  @JsonProperty("boxplot")
  FloatingBoxplot getBoxplot();

  @JsonProperty("boxplot")
  void setBoxplot(FloatingBoxplot boxplot);
}
