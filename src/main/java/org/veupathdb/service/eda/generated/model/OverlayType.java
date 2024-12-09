package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OverlayType {
  @JsonProperty("continuous")
  CONTINUOUS("continuous"),

  @JsonProperty("categorical")
  CATEGORICAL("categorical");

  public final String value;

  public String getValue() {
    return this.value;
  }

  OverlayType(String name) {
    this.value = name;
  }
}
