package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DifferentialExpressionMethod {
  @JsonProperty("DESeq")
  DESEQ("DESeq");

  public final String value;

  public String getValue() {
    return this.value;
  }

  DifferentialExpressionMethod(String name) {
    this.value = name;
  }
}
