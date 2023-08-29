package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortDirection {
  @JsonProperty("asc")
  ASC("asc"),

  @JsonProperty("desc")
  DESC("desc");

  public final String value;

  public String getValue() {
    return this.value;
  }

  SortDirection(String name) {
    this.value = name;
  }
}
