package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "showMissingness",
    "xAxisVariable",
    "geoAggregateVariable",
    "valueSpec",
    "longitudeVariable",
    "latitudeVariable",
    "viewport"
})
public class MapMarkersOverlaySpecImpl implements MapMarkersOverlaySpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("showMissingness")
  private ShowMissingness showMissingness;

  @JsonProperty("xAxisVariable")
  private VariableSpec xAxisVariable;

  @JsonProperty("geoAggregateVariable")
  private VariableSpec geoAggregateVariable;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

  @JsonProperty("longitudeVariable")
  private VariableSpec longitudeVariable;

  @JsonProperty("latitudeVariable")
  private VariableSpec latitudeVariable;

  @JsonProperty("viewport")
  private GeolocationViewport viewport;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("showMissingness")
  public ShowMissingness getShowMissingness() {
    return this.showMissingness;
  }

  @JsonProperty("showMissingness")
  public void setShowMissingness(ShowMissingness showMissingness) {
    this.showMissingness = showMissingness;
  }

  @JsonProperty("xAxisVariable")
  public VariableSpec getXAxisVariable() {
    return this.xAxisVariable;
  }

  @JsonProperty("xAxisVariable")
  public void setXAxisVariable(VariableSpec xAxisVariable) {
    this.xAxisVariable = xAxisVariable;
  }

  @JsonProperty("geoAggregateVariable")
  public VariableSpec getGeoAggregateVariable() {
    return this.geoAggregateVariable;
  }

  @JsonProperty("geoAggregateVariable")
  public void setGeoAggregateVariable(VariableSpec geoAggregateVariable) {
    this.geoAggregateVariable = geoAggregateVariable;
  }

  @JsonProperty("valueSpec")
  public ValueSpec getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(ValueSpec valueSpec) {
    this.valueSpec = valueSpec;
  }

  @JsonProperty("longitudeVariable")
  public VariableSpec getLongitudeVariable() {
    return this.longitudeVariable;
  }

  @JsonProperty("longitudeVariable")
  public void setLongitudeVariable(VariableSpec longitudeVariable) {
    this.longitudeVariable = longitudeVariable;
  }

  @JsonProperty("latitudeVariable")
  public VariableSpec getLatitudeVariable() {
    return this.latitudeVariable;
  }

  @JsonProperty("latitudeVariable")
  public void setLatitudeVariable(VariableSpec latitudeVariable) {
    this.latitudeVariable = latitudeVariable;
  }

  @JsonProperty("viewport")
  public GeolocationViewport getViewport() {
    return this.viewport;
  }

  @JsonProperty("viewport")
  public void setViewport(GeolocationViewport viewport) {
    this.viewport = viewport;
  }
}
