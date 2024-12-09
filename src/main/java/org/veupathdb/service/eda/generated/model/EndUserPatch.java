package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = EndUserPatchImpl.class
)
public interface EndUserPatch {
  @JsonProperty("op")
  OpType getOp();

  @JsonProperty("op")
  void setOp(OpType op);

  @JsonProperty("path")
  String getPath();

  @JsonProperty("path")
  void setPath(String path);

  @JsonProperty("value")
  Object getValue();

  @JsonProperty("value")
  void setValue(Object value);

  @JsonProperty("from")
  String getFrom();

  @JsonProperty("from")
  void setFrom(String from);

  enum OpType {
    @JsonProperty("add")
    ADD("add"),

    @JsonProperty("remove")
    REMOVE("remove"),

    @JsonProperty("replace")
    REPLACE("replace");

    public final String value;

    public String getValue() {
      return this.value;
    }

    OpType(String name) {
      this.value = name;
    }
  }
}
