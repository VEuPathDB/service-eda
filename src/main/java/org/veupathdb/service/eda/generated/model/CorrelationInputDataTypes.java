package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CorrelationInputDataTypes {
  @JsonProperty("metadata")
  METADATA("metadata"),

  @JsonProperty("collection")
  COLLECTION("collection");

  public final String value;

  public String getValue() {
    return this.value;
  }

  CorrelationInputDataTypes(String name) {
    this.value = name;
  }
}
