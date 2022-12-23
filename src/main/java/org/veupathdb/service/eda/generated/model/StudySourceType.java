package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StudySourceType {
  @JsonProperty("curated")
  CURATED("curated"),

  @JsonProperty("user_submitted")
  USERSUBMITTED("user_submitted");

  public final String name;

  StudySourceType(String name) {
    this.name = name;
  }


  public String getName() {
    return this.name;
  }
}
