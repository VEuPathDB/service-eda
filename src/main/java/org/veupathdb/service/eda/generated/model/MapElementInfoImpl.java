package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "geoAggregateValue",
    "entityCount",
    "avgLat",
    "avgLon",
    "minLat",
    "minLon",
    "maxLat",
    "maxLon"
})
public class MapElementInfoImpl implements MapElementInfo {
  @JsonProperty("geoAggregateValue")
  private String geoAggregateValue;

  @JsonProperty("entityCount")
  private Number entityCount;

  @JsonProperty("avgLat")
  private Number avgLat;

  @JsonProperty("avgLon")
  private Number avgLon;

  @JsonProperty("minLat")
  private Number minLat;

  @JsonProperty("minLon")
  private Number minLon;

  @JsonProperty("maxLat")
  private Number maxLat;

  @JsonProperty("maxLon")
  private Number maxLon;

  @JsonProperty("geoAggregateValue")
  public String getGeoAggregateValue() {
    return this.geoAggregateValue;
  }

  @JsonProperty("geoAggregateValue")
  public void setGeoAggregateValue(String geoAggregateValue) {
    this.geoAggregateValue = geoAggregateValue;
  }

  @JsonProperty("entityCount")
  public Number getEntityCount() {
    return this.entityCount;
  }

  @JsonProperty("entityCount")
  public void setEntityCount(Number entityCount) {
    this.entityCount = entityCount;
  }

  @JsonProperty("avgLat")
  public Number getAvgLat() {
    return this.avgLat;
  }

  @JsonProperty("avgLat")
  public void setAvgLat(Number avgLat) {
    this.avgLat = avgLat;
  }

  @JsonProperty("avgLon")
  public Number getAvgLon() {
    return this.avgLon;
  }

  @JsonProperty("avgLon")
  public void setAvgLon(Number avgLon) {
    this.avgLon = avgLon;
  }

  @JsonProperty("minLat")
  public Number getMinLat() {
    return this.minLat;
  }

  @JsonProperty("minLat")
  public void setMinLat(Number minLat) {
    this.minLat = minLat;
  }

  @JsonProperty("minLon")
  public Number getMinLon() {
    return this.minLon;
  }

  @JsonProperty("minLon")
  public void setMinLon(Number minLon) {
    this.minLon = minLon;
  }

  @JsonProperty("maxLat")
  public Number getMaxLat() {
    return this.maxLat;
  }

  @JsonProperty("maxLat")
  public void setMaxLat(Number maxLat) {
    this.maxLat = maxLat;
  }

  @JsonProperty("maxLon")
  public Number getMaxLon() {
    return this.maxLon;
  }

  @JsonProperty("maxLon")
  public void setMaxLon(Number maxLon) {
    this.maxLon = maxLon;
  }
}
