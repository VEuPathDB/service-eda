package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = LongitudeViewportImpl.class
)
public interface LongitudeViewport {
  @JsonProperty("left")
  Number getLeft();

  @JsonProperty("left")
  void setLeft(Number left);

  @JsonProperty("right")
  Number getRight();

  @JsonProperty("right")
  void setRight(Number right);
}
