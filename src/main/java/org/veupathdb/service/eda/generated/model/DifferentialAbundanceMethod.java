package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DifferentialAbundanceMethod {
  @JsonProperty("DESeq")
  DESEQ("DESeq"),

  @JsonProperty("ANCOMBC")
  ANCOMBC("ANCOMBC");

  public final String value;

  public String getValue() {
    return this.value;
  }

  DifferentialAbundanceMethod(String name) {
    this.value = name;
  }
}
