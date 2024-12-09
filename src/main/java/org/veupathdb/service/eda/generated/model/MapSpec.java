package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = MapSpecImpl.class
)
public interface MapSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("geoAggregateVariable")
  VariableSpec getGeoAggregateVariable();

  @JsonProperty("geoAggregateVariable")
  void setGeoAggregateVariable(VariableSpec geoAggregateVariable);

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
