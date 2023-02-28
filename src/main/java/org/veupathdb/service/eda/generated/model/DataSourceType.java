package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DataSourceType {
  @JsonProperty("database")
  DATABASE("database"),

  @JsonProperty("file")
  FILE("file");

  private final String value;

  public String getValue() {
    return this.value;
  }

  DataSourceType(String name) {
    this.value = name;
  }
}
