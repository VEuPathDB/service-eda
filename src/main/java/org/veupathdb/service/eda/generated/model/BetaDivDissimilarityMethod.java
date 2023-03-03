package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BetaDivDissimilarityMethod {
  @JsonProperty("bray")
  BRAY("bray"),

  @JsonProperty("jaccard")
  JACCARD("jaccard"),

  @JsonProperty("jsd")
  JSD("jsd");

  private final String value;

  public String getValue() {
    return this.value;
  }

  BetaDivDissimilarityMethod(String name) {
    this.value = name;
  }
}
