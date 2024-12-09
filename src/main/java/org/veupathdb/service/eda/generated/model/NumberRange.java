package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = NumberRangeImpl.class
)
public interface NumberRange {
  @JsonProperty("min")
  Number getMin();

  @JsonProperty("min")
  void setMin(Number min);

  @JsonProperty("max")
  Number getMax();

  @JsonProperty("max")
  void setMax(Number max);
}
