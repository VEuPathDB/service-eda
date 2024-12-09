package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "latitude",
    "longitude"
})
public class GeolocationViewportImpl implements GeolocationViewport {
  @JsonProperty("latitude")
  private NumericViewport latitude;

  @JsonProperty("longitude")
  private LongitudeViewport longitude;

  @JsonProperty("latitude")
  public NumericViewport getLatitude() {
    return this.latitude;
  }

  @JsonProperty("latitude")
  public void setLatitude(NumericViewport latitude) {
    this.latitude = latitude;
  }

  @JsonProperty("longitude")
  public LongitudeViewport getLongitude() {
    return this.longitude;
  }

  @JsonProperty("longitude")
  public void setLongitude(LongitudeViewport longitude) {
    this.longitude = longitude;
  }
}
