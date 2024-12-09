package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "config"
})
public class MapMarkersOverlayImpl implements MapMarkersOverlay {
  @JsonProperty("data")
  private List<MapMarkersOverlayData> data;

  @JsonProperty("config")
  private MapMarkersOverlayConfig config;

  @JsonProperty("data")
  public List<MapMarkersOverlayData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<MapMarkersOverlayData> data) {
    this.data = data;
  }

  @JsonProperty("config")
  public MapMarkersOverlayConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(MapMarkersOverlayConfig config) {
    this.config = config;
  }
}
