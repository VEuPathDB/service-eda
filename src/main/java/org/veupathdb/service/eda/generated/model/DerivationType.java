package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DerivationType {
  @JsonProperty("transform")
  TRANSFORM("transform"),

  @JsonProperty("reduction")
  REDUCTION("reduction");

  public final String name;

  DerivationType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
