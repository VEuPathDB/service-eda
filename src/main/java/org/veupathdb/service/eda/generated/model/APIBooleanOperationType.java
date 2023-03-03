package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APIBooleanOperationType {
  @JsonProperty("intersect")
  INTERSECT("intersect"),

  @JsonProperty("union")
  UNION("union");

  private final String value;

  public String getValue() {
    return this.value;
  }

  APIBooleanOperationType(String name) {
    this.value = name;
  }
}
