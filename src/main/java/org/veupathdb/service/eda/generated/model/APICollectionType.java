package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APICollectionType {
  @JsonProperty("number")
  NUMBER("number"),

  @JsonProperty("date")
  DATE("date"),

  @JsonProperty("integer")
  INTEGER("integer");

  public final String name;

  APICollectionType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
