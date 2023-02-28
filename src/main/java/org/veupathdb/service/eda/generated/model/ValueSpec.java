package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ValueSpec {
  @JsonProperty("count")
  COUNT("count"),

  @JsonProperty("proportion")
  PROPORTION("proportion");

  private final String value;

  public String getValue() {
    return this.value;
  }

  ValueSpec(String name) {
    this.value = name;
  }
}
