package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DateRangeImpl.class
)
public interface DateRange {
  @JsonProperty("min")
  String getMin();

  @JsonProperty("min")
  void setMin(String min);

  @JsonProperty("max")
  String getMax();

  @JsonProperty("max")
  void setMax(String max);
}
