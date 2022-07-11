package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APIVariableType {
  @JsonProperty("category")
  CATEGORY("category"),

  @JsonProperty("string")
  STRING("string"),

  @JsonProperty("number")
  NUMBER("number"),

  @JsonProperty("date")
  DATE("date"),

  @JsonProperty("longitude")
  LONGITUDE("longitude"),

  @JsonProperty("integer")
  INTEGER("integer");

  public final String name;

  APIVariableType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
