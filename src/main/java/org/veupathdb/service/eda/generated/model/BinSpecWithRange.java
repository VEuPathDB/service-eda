package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = BinSpecWithRangeImpl.class
)
public interface BinSpecWithRange {
  @JsonProperty("displayRangeMin")
  Object getDisplayRangeMin();

  @JsonProperty("displayRangeMin")
  void setDisplayRangeMin(Object displayRangeMin);

  @JsonProperty("displayRangeMax")
  Object getDisplayRangeMax();

  @JsonProperty("displayRangeMax")
  void setDisplayRangeMax(Object displayRangeMax);

  @JsonProperty("binWidth")
  Number getBinWidth();

  @JsonProperty("binWidth")
  void setBinWidth(Number binWidth);

  @JsonProperty("binUnits")
  BinUnits getBinUnits();

  @JsonProperty("binUnits")
  void setBinUnits(BinUnits binUnits);
}
