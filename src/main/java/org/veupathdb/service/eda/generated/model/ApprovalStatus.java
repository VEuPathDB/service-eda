package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ApprovalStatus {
  @JsonProperty("unrequested")
  UNREQUESTED("unrequested"),

  @JsonProperty("approved")
  APPROVED("approved"),

  @JsonProperty("requested")
  REQUESTED("requested"),

  @JsonProperty("denied")
  DENIED("denied");

  public final String value;

  public String getValue() {
    return this.value;
  }

  ApprovalStatus(String name) {
    this.value = name;
  }
}
