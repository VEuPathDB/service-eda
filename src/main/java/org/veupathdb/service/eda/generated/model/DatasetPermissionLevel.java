package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DatasetPermissionLevel {
  @JsonProperty("provider")
  PROVIDER("provider"),

  @JsonProperty("end-user")
  ENDUSER("end-user");

  public final String value;

  public String getValue() {
    return this.value;
  }

  DatasetPermissionLevel(String name) {
    this.value = name;
  }
}
