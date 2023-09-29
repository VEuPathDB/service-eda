package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StudySourceType {
  @JsonProperty("curated")
  CURATED("curated"),

  @JsonProperty("user_submitted")
  USERSUBMITTED("user_submitted");

  public final String value;

  public String getValue() {
    return this.value;
  }

  StudySourceType(String name) {
    this.value = name;
  }
}
