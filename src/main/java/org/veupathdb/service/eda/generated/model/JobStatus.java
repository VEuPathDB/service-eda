package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum JobStatus {
  @JsonProperty("no-such-job")
  NOSUCHJOB("no-such-job"),

  @JsonProperty("queued")
  QUEUED("queued"),

  @JsonProperty("in-progress")
  INPROGRESS("in-progress"),

  @JsonProperty("complete")
  COMPLETE("complete"),

  @JsonProperty("failed")
  FAILED("failed"),

  @JsonProperty("expired")
  EXPIRED("expired");

  private final String value;

  public String getValue() {
    return this.value;
  }

  JobStatus(String name) {
    this.value = name;
  }
}
