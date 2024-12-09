package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AlphaDivMethod {
  @JsonProperty("shannon")
  SHANNON("shannon"),

  @JsonProperty("simpson")
  SIMPSON("simpson"),

  @JsonProperty("evenness")
  EVENNESS("evenness");

  public final String value;

  public String getValue() {
    return this.value;
  }

  AlphaDivMethod(String name) {
    this.value = name;
  }
}
