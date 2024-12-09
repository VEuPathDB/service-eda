package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = StandaloneMapMarkersPostResponseImpl.class
)
public interface StandaloneMapMarkersPostResponse {
  @JsonProperty("mapElements")
  List<StandaloneMapElementInfo> getMapElements();

  @JsonProperty("mapElements")
  void setMapElements(List<StandaloneMapElementInfo> mapElements);
}
