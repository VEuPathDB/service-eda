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

  public final String name;

  RankingMethod(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
