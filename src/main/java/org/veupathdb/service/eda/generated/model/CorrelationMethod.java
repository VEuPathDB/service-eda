package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CorrelationMethod {
  @JsonProperty("spearman")
  SPEARMAN("spearman"),

  @JsonProperty("pearson")
  PEARSON("pearson");

  public final String value;

  public String getValue() {
    return this.value;
  }

  CorrelationMethod(String name) {
    this.value = name;
  }
}
