package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FileImpl.class
)
public interface File {
  @JsonProperty("name")
  String getName();

  @JsonProperty("name")
  void setName(String name);

  @JsonProperty("modifiedDate")
  String getModifiedDate();

  @JsonProperty("modifiedDate")
  void setModifiedDate(String modifiedDate);

  @JsonProperty("size")
  String getSize();

  @JsonProperty("size")
  void setSize(String size);
}
