package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TabularHeaderFormat {
  @JsonProperty("standard")
  STANDARD("standard"),

  @JsonProperty("display")
  DISPLAY("display");

  private final String value;

  public String getValue() {
    return this.value;
  }

  TabularHeaderFormat(String name) {
    this.value = name;
  }
}
