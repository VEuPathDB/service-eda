package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MetricsUserProjectIdAnalysesGetStudyType {
  @JsonProperty("ALL")
  ALL("ALL"),

  @JsonProperty("USER")
  USER("USER"),

  @JsonProperty("CURATED")
  CURATED("CURATED");

  public final String value;

  public String getValue() {
    return this.value;
  }

  MetricsUserProjectIdAnalysesGetStudyType(String name) {
    this.value = name;
  }
}
