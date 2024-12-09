package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SetOperation {
  @JsonProperty("intersect")
  INTERSECT("intersect"),

  @JsonProperty("union")
  UNION("union"),

  @JsonProperty("minus")
  MINUS("minus");

  public final String value;

  public String getValue() {
    return this.value;
  }

  SetOperation(String name) {
    this.value = name;
  }
}
