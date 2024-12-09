package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StringBoolean {
  @JsonProperty("TRUE")
  TRUE("TRUE"),

  @JsonProperty("FALSE")
  FALSE("FALSE");

  public final String value;

  public String getValue() {
    return this.value;
  }

  StringBoolean(String name) {
    this.value = name;
  }
}
