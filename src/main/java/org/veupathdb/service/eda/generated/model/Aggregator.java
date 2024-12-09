package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Aggregator {
  @JsonProperty("mean")
  MEAN("mean"),

  @JsonProperty("median")
  MEDIAN("median");

  public final String value;

  public String getValue() {
    return this.value;
  }

  Aggregator(String name) {
    this.value = name;
  }
}
