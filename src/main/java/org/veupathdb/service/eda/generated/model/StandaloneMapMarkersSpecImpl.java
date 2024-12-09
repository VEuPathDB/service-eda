package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "geoAggregateVariable",
    "longitudeVariable",
    "latitudeVariable",
    "overlayConfig",
    "valueSpec",
    "viewport"
})
public class StandaloneMapMarkersSpecImpl implements StandaloneMapMarkersSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("geoAggregateVariable")
  private VariableSpec geoAggregateVariable;

  @JsonProperty("longitudeVariable")
  private VariableSpec longitudeVariable;

  @JsonProperty("latitudeVariable")
  private VariableSpec latitudeVariable;

  @JsonProperty("overlayConfig")
  private OverlayConfig overlayConfig;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

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

  @JsonProperty("geoAggregateVariable")
  public VariableSpec getGeoAggregateVariable() {
    return this.geoAggregateVariable;
  }

  @JsonProperty("geoAggregateVariable")
  public void setGeoAggregateVariable(VariableSpec geoAggregateVariable) {
    this.geoAggregateVariable = geoAggregateVariable;
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

  @JsonProperty("overlayConfig")
  public OverlayConfig getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(OverlayConfig overlayConfig) {
    this.overlayConfig = overlayConfig;
  }

  @JsonProperty("valueSpec")
  public ValueSpec getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(ValueSpec valueSpec) {
    this.valueSpec = valueSpec;
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
