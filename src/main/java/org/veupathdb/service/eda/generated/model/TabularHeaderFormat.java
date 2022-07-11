package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TabularHeaderFormat {
  @JsonProperty("standard")
  STANDARD("standard"),

  @JsonProperty("display")
  DISPLAY("display");

  public final String name;

  TabularHeaderFormat(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
