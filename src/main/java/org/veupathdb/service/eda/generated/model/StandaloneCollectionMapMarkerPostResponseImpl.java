package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("markers")
public class StandaloneCollectionMapMarkerPostResponseImpl implements StandaloneCollectionMapMarkerPostResponse {
  @JsonProperty("markers")
  private List<CollectionMapMarkerElement> markers;

  @JsonProperty("markers")
  public List<CollectionMapMarkerElement> getMarkers() {
    return this.markers;
  }

  @JsonProperty("markers")
  public void setMarkers(List<CollectionMapMarkerElement> markers) {
    this.markers = markers;
  }
}
