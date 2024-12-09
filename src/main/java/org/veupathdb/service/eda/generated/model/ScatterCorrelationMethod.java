package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ScatterCorrelationMethod {
  @JsonProperty("none")
  NONE("none"),

  @JsonProperty("spearman")
  SPEARMAN("spearman"),

  @JsonProperty("pearson")
  PEARSON("pearson"),

  @JsonProperty("sparcc")
  SPARCC("sparcc");

  public final String value;

  public String getValue() {
    return this.value;
  }

  ScatterCorrelationMethod(String name) {
    this.value = name;
  }
}
