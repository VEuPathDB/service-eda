package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RestrictionLevel {
  @JsonProperty("public")
  PUBLIC("public"),

  @JsonProperty("prerelease")
  PRERELEASE("prerelease"),

  @JsonProperty("protected")
  PROTECTED("protected"),

  @JsonProperty("controlled")
  CONTROLLED("controlled"),

  @JsonProperty("private")
  PRIVATE("private");

  public final String value;

  public String getValue() {
    return this.value;
  }

  RestrictionLevel(String name) {
    this.value = name;
  }
}
