package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APICollectionType {
  @JsonProperty("number")
  NUMBER("number"),

  @JsonProperty("date")
  DATE("date"),

  @JsonProperty("integer")
  INTEGER("integer");

  private final String value;

  public String getValue() {
    return this.value;
  }

  APICollectionType(String name) {
    this.value = name;
  }
}
