package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlotReferenceValue {
  @JsonProperty("xAxis")
  XAXIS("xAxis"),

  @JsonProperty("yAxis")
  YAXIS("yAxis"),

  @JsonProperty("zAxis")
  ZAXIS("zAxis"),

  @JsonProperty("overlay")
  OVERLAY("overlay"),

  @JsonProperty("facet1")
  FACET1("facet1"),

  @JsonProperty("facet2")
  FACET2("facet2"),

  @JsonProperty("geo")
  GEO("geo"),

  @JsonProperty("latitude")
  LATITUDE("latitude"),

  @JsonProperty("longitude")
  LONGITUDE("longitude");

  public final String name;

  PlotReferenceValue(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
