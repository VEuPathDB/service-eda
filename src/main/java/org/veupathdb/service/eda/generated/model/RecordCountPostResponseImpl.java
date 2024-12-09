package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("recordCount")
public class RecordCountPostResponseImpl implements RecordCountPostResponse {
  @JsonProperty("recordCount")
  private Integer recordCount;

  @JsonProperty("recordCount")
  public Integer getRecordCount() {
    return this.recordCount;
  }

  @JsonProperty("recordCount")
  public void setRecordCount(Integer recordCount) {
    this.recordCount = recordCount;
  }
}
