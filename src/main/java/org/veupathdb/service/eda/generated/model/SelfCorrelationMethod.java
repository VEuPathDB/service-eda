package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SelfCorrelationMethod {
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

  SelfCorrelationMethod(String name) {
    this.value = name;
  }
}
