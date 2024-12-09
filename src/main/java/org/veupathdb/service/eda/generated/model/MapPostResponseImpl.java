package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mapElements",
    "config"
})
public class MapPostResponseImpl implements MapPostResponse {
  @JsonProperty("mapElements")
  private List<MapElementInfo> mapElements;

  @JsonProperty("config")
  private MapConfig config;

  @JsonProperty("mapElements")
  public List<MapElementInfo> getMapElements() {
    return this.mapElements;
  }

  @JsonProperty("mapElements")
  public void setMapElements(List<MapElementInfo> mapElements) {
    this.mapElements = mapElements;
  }

  @JsonProperty("config")
  public MapConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(MapConfig config) {
    this.config = config;
  }
}
