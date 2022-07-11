package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortDirection {
  @JsonProperty("asc")
  ASC("asc"),

  @JsonProperty("desc")
  DESC("desc");

  public final String name;

  SortDirection(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
