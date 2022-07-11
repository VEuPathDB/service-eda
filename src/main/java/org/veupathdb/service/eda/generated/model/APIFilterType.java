package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APIFilterType {
  @JsonProperty("stringSet")
  STRINGSET("stringSet"),

  @JsonProperty("numberSet")
  NUMBERSET("numberSet"),

  @JsonProperty("dateSet")
  DATESET("dateSet"),

  @JsonProperty("numberRange")
  NUMBERRANGE("numberRange"),

  @JsonProperty("dateRange")
  DATERANGE("dateRange"),

  @JsonProperty("longitudeRange")
  LONGITUDERANGE("longitudeRange"),

  @JsonProperty("multiFilter")
  MULTIFILTER("multiFilter");

  public final String name;

  APIFilterType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
