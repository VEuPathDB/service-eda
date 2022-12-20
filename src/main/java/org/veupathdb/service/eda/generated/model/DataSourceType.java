package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DataSourceType {
  @JsonProperty("database")
  DATABASE("database"),

  @JsonProperty("file")
  FILE("file");

  public final String name;

  DataSourceType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
