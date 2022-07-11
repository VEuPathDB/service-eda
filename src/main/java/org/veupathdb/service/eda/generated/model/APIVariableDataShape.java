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

  public final String name;

  APIVariableDataShape(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
