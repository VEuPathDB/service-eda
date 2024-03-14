package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "displayRangeMin",
    "displayRangeMax",
    "binWidth",
    "binUnits"
})
public class BinSpecWithRangeImpl implements BinSpecWithRange {
  @JsonProperty("displayRangeMin")
  private Object displayRangeMin;

  @JsonProperty("displayRangeMax")
  private Object displayRangeMax;

  @JsonProperty("binWidth")
  private Number binWidth;

  @JsonProperty("binUnits")
  private BinUnits binUnits;

  @JsonProperty("displayRangeMin")
  public Object getDisplayRangeMin() {
    return this.displayRangeMin;
  }

  @JsonProperty("displayRangeMin")
  public void setDisplayRangeMin(Object displayRangeMin) {
    this.displayRangeMin = displayRangeMin;
  }

  @JsonProperty("displayRangeMax")
  public Object getDisplayRangeMax() {
    return this.displayRangeMax;
  }

  @JsonProperty("displayRangeMax")
  public void setDisplayRangeMax(Object displayRangeMax) {
    this.displayRangeMax = displayRangeMax;
  }

  @JsonProperty("binWidth")
  public Number getBinWidth() {
    return this.binWidth;
  }

  @JsonProperty("binWidth")
  public void setBinWidth(Number binWidth) {
    this.binWidth = binWidth;
  }

  @JsonProperty("binUnits")
  public BinUnits getBinUnits() {
    return this.binUnits;
  }

  @JsonProperty("binUnits")
  public void setBinUnits(BinUnits binUnits) {
    this.binUnits = binUnits;
  }
}
