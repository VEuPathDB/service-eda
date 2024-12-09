package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("staffId")
public class NewStaffResponseImpl implements NewStaffResponse {
  @JsonProperty("staffId")
  private Long staffId;

  @JsonProperty("staffId")
  public Long getStaffId() {
    return this.staffId;
  }

  @JsonProperty("staffId")
  public void setStaffId(Long staffId) {
    this.staffId = staffId;
  }
}
