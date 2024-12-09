package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = GeolocationViewportImpl.class
)
public interface GeolocationViewport {
  @JsonProperty("latitude")
  NumericViewport getLatitude();

  @JsonProperty("latitude")
  void setLatitude(NumericViewport latitude);

  @JsonProperty("longitude")
  LongitudeViewport getLongitude();

  @JsonProperty("longitude")
  void setLongitude(LongitudeViewport longitude);
}
