package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum APIVariableScale {
  @JsonProperty("log")
  LOG("log"),

  @JsonProperty("log2")
  LOG2("log2"),

  @JsonProperty("ln")
  LN("ln");

  public final String value;

  public String getValue() {
    return this.value;
  }

  APIVariableScale(String name) {
    this.value = name;
  }
}
