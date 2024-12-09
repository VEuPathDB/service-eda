package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MapMarkersOverlayPostResponseImpl.class
)
public interface MapMarkersOverlayPostResponse {
  @JsonProperty("mapMarkers")
  MapMarkersOverlay getMapMarkers();

  @JsonProperty("mapMarkers")
  void setMapMarkers(MapMarkersOverlay mapMarkers);

  @JsonProperty("sampleSizeTable")
  List<SampleSizeTable> getSampleSizeTable();

  @JsonProperty("sampleSizeTable")
  void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable);

  @JsonProperty("completeCasesTable")
  List<VariableCompleteCases> getCompleteCasesTable();

  @JsonProperty("completeCasesTable")
  void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable);
}
