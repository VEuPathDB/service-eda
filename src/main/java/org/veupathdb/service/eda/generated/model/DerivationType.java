package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DerivationType {
  @JsonProperty("transform")
  TRANSFORM("transform"),

  @JsonProperty("reduction")
  REDUCTION("reduction");

  public final String value;

  public String getValue() {
    return this.value;
  }

  DerivationType(String name) {
    this.value = name;
  }
}
