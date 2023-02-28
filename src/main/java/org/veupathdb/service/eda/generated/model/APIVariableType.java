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

  private final String value;

  public String getValue() {
    return this.value;
  }

  APIVariableType(String name) {
    this.value = name;
  }
}
