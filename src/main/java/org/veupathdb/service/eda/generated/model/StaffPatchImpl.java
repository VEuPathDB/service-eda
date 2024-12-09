package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "op",
    "path",
    "value"
})
public class StaffPatchImpl implements StaffPatch {
  @JsonProperty("op")
  private String op;

  @JsonProperty("path")
  private String path;

  @JsonProperty("value")
  private Boolean value;

  @JsonProperty("op")
  public String getOp() {
    return this.op;
  }

  @JsonProperty("op")
  public void setOp(String op) {
    this.op = op;
  }

  @JsonProperty("path")
  public String getPath() {
    return this.path;
  }

  @JsonProperty("path")
  public void setPath(String path) {
    this.path = path;
  }

  @JsonProperty("value")
  public Boolean getValue() {
    return this.value;
  }

  @JsonProperty("value")
  public void setValue(Boolean value) {
    this.value = value;
  }
}
