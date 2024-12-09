package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "minColorValue",
    "maxColorValue",
    "minSizeValue",
    "maxSizeValue"
})
public class StandaloneMapBubblesLegendPostResponseImpl implements StandaloneMapBubblesLegendPostResponse {
  @JsonProperty("minColorValue")
  private String minColorValue;

  @JsonProperty("maxColorValue")
  private String maxColorValue;

  @JsonProperty("minSizeValue")
  private Number minSizeValue;

  @JsonProperty("maxSizeValue")
  private Number maxSizeValue;

  @JsonProperty("minColorValue")
  public String getMinColorValue() {
    return this.minColorValue;
  }

  @JsonProperty("minColorValue")
  public void setMinColorValue(String minColorValue) {
    this.minColorValue = minColorValue;
  }

  @JsonProperty("maxColorValue")
  public String getMaxColorValue() {
    return this.maxColorValue;
  }

  @JsonProperty("maxColorValue")
  public void setMaxColorValue(String maxColorValue) {
    this.maxColorValue = maxColorValue;
  }

  @JsonProperty("minSizeValue")
  public Number getMinSizeValue() {
    return this.minSizeValue;
  }

  @JsonProperty("minSizeValue")
  public void setMinSizeValue(Number minSizeValue) {
    this.minSizeValue = minSizeValue;
  }

  @JsonProperty("maxSizeValue")
  public Number getMaxSizeValue() {
    return this.maxSizeValue;
  }

  @JsonProperty("maxSizeValue")
  public void setMaxSizeValue(Number maxSizeValue) {
    this.maxSizeValue = maxSizeValue;
  }
}
