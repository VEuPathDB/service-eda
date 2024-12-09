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
    "viewport",
    "collectionOverlay",
    "aggregatorConfig"
})
public class StandaloneCollectionMapMarkerSpecImpl implements StandaloneCollectionMapMarkerSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("geoAggregateVariable")
  private VariableSpec geoAggregateVariable;

  @JsonProperty("longitudeVariable")
  private VariableSpec longitudeVariable;

  @JsonProperty("latitudeVariable")
  private VariableSpec latitudeVariable;

  @JsonProperty("viewport")
  private GeolocationViewport viewport;

  @JsonProperty("collectionOverlay")
  private CollectionOverlayConfig collectionOverlay;

  @JsonProperty("aggregatorConfig")
  private QuantitativeAggregationConfig aggregatorConfig;

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

  @JsonProperty("viewport")
  public GeolocationViewport getViewport() {
    return this.viewport;
  }

  @JsonProperty("viewport")
  public void setViewport(GeolocationViewport viewport) {
    this.viewport = viewport;
  }

  @JsonProperty("collectionOverlay")
  public CollectionOverlayConfig getCollectionOverlay() {
    return this.collectionOverlay;
  }

  @JsonProperty("collectionOverlay")
  public void setCollectionOverlay(CollectionOverlayConfig collectionOverlay) {
    this.collectionOverlay = collectionOverlay;
  }

  @JsonProperty("aggregatorConfig")
  public QuantitativeAggregationConfig getAggregatorConfig() {
    return this.aggregatorConfig;
  }

  @JsonProperty("aggregatorConfig")
  public void setAggregatorConfig(QuantitativeAggregationConfig aggregatorConfig) {
    this.aggregatorConfig = aggregatorConfig;
  }
}
