package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = MapMarkersOverlaySpecImpl.class
)
public interface MapMarkersOverlaySpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("showMissingness")
  ShowMissingness getShowMissingness();

  @JsonProperty("showMissingness")
  void setShowMissingness(ShowMissingness showMissingness);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("geoAggregateVariable")
  VariableSpec getGeoAggregateVariable();

  @JsonProperty("geoAggregateVariable")
  void setGeoAggregateVariable(VariableSpec geoAggregateVariable);

  @JsonProperty("valueSpec")
  ValueSpec getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpec valueSpec);

  @JsonProperty("longitudeVariable")
  VariableSpec getLongitudeVariable();

  @JsonProperty("longitudeVariable")
  void setLongitudeVariable(VariableSpec longitudeVariable);

  @JsonProperty("latitudeVariable")
  VariableSpec getLatitudeVariable();

  @JsonProperty("latitudeVariable")
  void setLatitudeVariable(VariableSpec latitudeVariable);

  @JsonProperty("viewport")
  GeolocationViewport getViewport();

  @JsonProperty("viewport")
  void setViewport(GeolocationViewport viewport);
}
