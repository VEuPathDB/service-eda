package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = StandaloneCollectionMapMarkerPostResponseImpl.class
)
public interface StandaloneCollectionMapMarkerPostResponse {
  @JsonProperty("markers")
  List<CollectionMapMarkerElement> getMarkers();

  @JsonProperty("markers")
  void setMarkers(List<CollectionMapMarkerElement> markers);
}
