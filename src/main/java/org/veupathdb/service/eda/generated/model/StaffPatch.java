package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StaffPatchImpl.class
)
public interface StaffPatch {
  @JsonProperty("op")
  String getOp();

  @JsonProperty("op")
  void setOp(String op);

  @JsonProperty("path")
  String getPath();

  @JsonProperty("path")
  void setPath(String path);

  @JsonProperty("value")
  Boolean getValue();

  @JsonProperty("value")
  void setValue(Boolean value);
}
