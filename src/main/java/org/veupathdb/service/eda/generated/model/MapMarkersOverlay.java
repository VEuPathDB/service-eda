package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MapMarkersOverlayImpl.class
)
public interface MapMarkersOverlay {
  @JsonProperty("data")
  List<MapMarkersOverlayData> getData();

  @JsonProperty("data")
  void setData(List<MapMarkersOverlayData> data);

  @JsonProperty("config")
  MapMarkersOverlayConfig getConfig();

  @JsonProperty("config")
  void setConfig(MapMarkersOverlayConfig config);
}
