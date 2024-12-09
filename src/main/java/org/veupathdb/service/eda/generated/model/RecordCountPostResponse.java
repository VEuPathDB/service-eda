package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = RecordCountPostResponseImpl.class
)
public interface RecordCountPostResponse {
  @JsonProperty("recordCount")
  Integer getRecordCount();

  @JsonProperty("recordCount")
  void setRecordCount(Integer recordCount);
}
