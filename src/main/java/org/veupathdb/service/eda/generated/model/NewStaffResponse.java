package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = NewStaffResponseImpl.class
)
public interface NewStaffResponse {
  @JsonProperty("staffId")
  Long getStaffId();

  @JsonProperty("staffId")
  void setStaffId(Long staffId);
}
