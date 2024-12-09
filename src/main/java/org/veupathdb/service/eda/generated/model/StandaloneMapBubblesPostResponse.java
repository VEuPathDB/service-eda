package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = StandaloneMapBubblesPostResponseImpl.class
)
public interface StandaloneMapBubblesPostResponse {
  @JsonProperty("mapElements")
  List<ColoredMapElementInfo> getMapElements();

  @JsonProperty("mapElements")
  void setMapElements(List<ColoredMapElementInfo> mapElements);
}
