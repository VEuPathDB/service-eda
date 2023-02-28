package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RankingMethod {
  @JsonProperty("median")
  MEDIAN("median"),

  @JsonProperty("q3")
  Q3("q3"),

  @JsonProperty("variance")
  VARIANCE("variance"),

  @JsonProperty("max")
  MAX("max");

  private final String value;

  public String getValue() {
    return this.value;
  }

  RankingMethod(String name) {
    this.value = name;
  }
}
