package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MapPostResponseImpl.class
)
public interface MapPostResponse {
  @JsonProperty("mapElements")
  List<MapElementInfo> getMapElements();

  @JsonProperty("mapElements")
  void setMapElements(List<MapElementInfo> mapElements);

  @JsonProperty("config")
  MapConfig getConfig();

  @JsonProperty("config")
  void setConfig(MapConfig config);
}
