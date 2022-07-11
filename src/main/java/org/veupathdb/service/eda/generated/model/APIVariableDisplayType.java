package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APIVariableDisplayType {
  @JsonProperty("default")
  DEFAULT("default"),

  @JsonProperty("hidden")
  HIDDEN("hidden"),

  @JsonProperty("multifilter")
  MULTIFILTER("multifilter"),

  @JsonProperty("geoaggregator")
  GEOAGGREGATOR("geoaggregator"),

  @JsonProperty("latitude")
  LATITUDE("latitude"),

  @JsonProperty("longitude")
  LONGITUDE("longitude");

  public final String name;

  APIVariableDisplayType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
