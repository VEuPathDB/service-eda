package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "displayRangeMin",
    "displayRangeMax",
    "rangeMin",
    "rangeMax",
    "binWidth",
    "binWidthOverride"
})
public class APINumberDistributionDefaultsImpl implements APINumberDistributionDefaults {
  @JsonProperty("displayRangeMin")
  private Number displayRangeMin;

  @JsonProperty("displayRangeMax")
  private Number displayRangeMax;

  @JsonProperty("rangeMin")
  private Number rangeMin;

  @JsonProperty("rangeMax")
  private Number rangeMax;

  @JsonProperty("binWidth")
  private Number binWidth;

  @JsonProperty("binWidthOverride")
  private Number binWidthOverride;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("displayRangeMin")
  public Number getDisplayRangeMin() {
    return this.displayRangeMin;
  }

  @JsonProperty("displayRangeMin")
  public void setDisplayRangeMin(Number displayRangeMin) {
    this.displayRangeMin = displayRangeMin;
  }

  @JsonProperty("displayRangeMax")
  public Number getDisplayRangeMax() {
    return this.displayRangeMax;
  }

  @JsonProperty("displayRangeMax")
  public void setDisplayRangeMax(Number displayRangeMax) {
    this.displayRangeMax = displayRangeMax;
  }

  @JsonProperty("rangeMin")
  public Number getRangeMin() {
    return this.rangeMin;
  }

  @JsonProperty("rangeMin")
  public void setRangeMin(Number rangeMin) {
    this.rangeMin = rangeMin;
  }

  @JsonProperty("rangeMax")
  public Number getRangeMax() {
    return this.rangeMax;
  }

  @JsonProperty("rangeMax")
  public void setRangeMax(Number rangeMax) {
    this.rangeMax = rangeMax;
  }

  @JsonProperty("binWidth")
  public Number getBinWidth() {
    return this.binWidth;
  }

  @JsonProperty("binWidth")
  public void setBinWidth(Number binWidth) {
    this.binWidth = binWidth;
  }

  @JsonProperty("binWidthOverride")
  public Number getBinWidthOverride() {
    return this.binWidthOverride;
  }

  @JsonProperty("binWidthOverride")
  public void setBinWidthOverride(Number binWidthOverride) {
    this.binWidthOverride = binWidthOverride;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
