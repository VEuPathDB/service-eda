package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BetaDivDistanceMethod {
  @JsonProperty("bray")
  BRAY("bray"),

  @JsonProperty("jaccard")
  JACCARD("jaccard"),

  @JsonProperty("jsd")
  JSD("jsd");

  public final String name;

  BetaDivDistanceMethod(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
