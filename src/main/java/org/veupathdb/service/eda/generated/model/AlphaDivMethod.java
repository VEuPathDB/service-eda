package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AlphaDivMethod {
  @JsonProperty("shannon")
  SHANNON("shannon"),

  @JsonProperty("simpson")
  SIMPSON("simpson"),

  @JsonProperty("evenness")
  EVENNESS("evenness");

  public final String name;

  AlphaDivMethod(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
