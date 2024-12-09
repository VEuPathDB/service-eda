package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = SimpleSampleSizeImpl.class
)
public interface SimpleSampleSize {
  @JsonProperty("N")
  Number getN();

  @JsonProperty("N")
  void setN(Number n);
}
