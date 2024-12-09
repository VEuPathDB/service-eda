package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShowMissingnessNoAxes {
  @JsonProperty("strataVariables")
  STRATAVARIABLES("strataVariables"),

  @JsonProperty("noVariables")
  NOVARIABLES("noVariables"),

  @JsonProperty("TRUE")
  TRUE("TRUE"),

  @JsonProperty("FALSE")
  FALSE("FALSE");

  public final String value;

  public String getValue() {
    return this.value;
  }

  ShowMissingnessNoAxes(String name) {
    this.value = name;
  }
}
