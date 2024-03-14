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

  public final String value;

  public String getValue() {
    return this.value;
  }

  APIVariableDisplayType(String name) {
    this.value = name;
  }
}
