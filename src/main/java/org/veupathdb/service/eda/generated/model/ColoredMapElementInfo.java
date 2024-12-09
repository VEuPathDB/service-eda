package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ColoredMapElementInfoImpl.class
)
public interface ColoredMapElementInfo extends BaseMarker {
  @JsonProperty("geoAggregateValue")
  String getGeoAggregateValue();

  @JsonProperty("geoAggregateValue")
  void setGeoAggregateValue(String geoAggregateValue);

  @JsonProperty("entityCount")
  Number getEntityCount();

  @JsonProperty("entityCount")
  void setEntityCount(Number entityCount);

  @JsonProperty("avgLat")
  Number getAvgLat();

  @JsonProperty("avgLat")
  void setAvgLat(Number avgLat);

  @JsonProperty("avgLon")
  Number getAvgLon();

  @JsonProperty("avgLon")
  void setAvgLon(Number avgLon);

  @JsonProperty("minLat")
  Number getMinLat();

  @JsonProperty("minLat")
  void setMinLat(Number minLat);

  @JsonProperty("minLon")
  Number getMinLon();

  @JsonProperty("minLon")
  void setMinLon(Number minLon);

  @JsonProperty("maxLat")
  Number getMaxLat();

  @JsonProperty("maxLat")
  void setMaxLat(Number maxLat);

  @JsonProperty("maxLon")
  Number getMaxLon();

  @JsonProperty("maxLon")
  void setMaxLon(Number maxLon);

  @JsonProperty("overlayValue")
  String getOverlayValue();

  @JsonProperty("overlayValue")
  void setOverlayValue(String overlayValue);
}
