package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum VariableClass {
  @JsonProperty("native")
  NATIVE("native"),

  @JsonProperty("derived")
  DERIVED("derived"),

  @JsonProperty("computed")
  COMPUTED("computed");

  public final String value;

  public String getValue() {
    return this.value;
  }

  VariableClass(String name) {
    this.value = name;
  }
}
