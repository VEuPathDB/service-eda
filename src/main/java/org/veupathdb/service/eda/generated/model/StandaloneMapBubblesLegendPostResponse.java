package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StandaloneMapBubblesLegendPostResponseImpl.class
)
public interface StandaloneMapBubblesLegendPostResponse {
  @JsonProperty("minColorValue")
  String getMinColorValue();

  @JsonProperty("minColorValue")
  void setMinColorValue(String minColorValue);

  @JsonProperty("maxColorValue")
  String getMaxColorValue();

  @JsonProperty("maxColorValue")
  void setMaxColorValue(String maxColorValue);

  @JsonProperty("minSizeValue")
  Number getMinSizeValue();

  @JsonProperty("minSizeValue")
  void setMinSizeValue(Number minSizeValue);

  @JsonProperty("maxSizeValue")
  Number getMaxSizeValue();

  @JsonProperty("maxSizeValue")
  void setMaxSizeValue(Number maxSizeValue);
}
