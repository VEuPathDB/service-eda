package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APIVariableDataShape {
  @JsonProperty("continuous")
  CONTINUOUS("continuous"),

  @JsonProperty("categorical")
  CATEGORICAL("categorical"),

  @JsonProperty("ordinal")
  ORDINAL("ordinal"),

  @JsonProperty("binary")
  BINARY("binary");

  private final String value;

  public String getValue() {
    return this.value;
  }

  APIVariableDataShape(String name) {
    this.value = name;
  }
}
