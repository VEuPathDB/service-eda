package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = EndUserCreateResponseImpl.class
)
public interface EndUserCreateResponse {
  @JsonProperty("created")
  Boolean getCreated();

  @JsonProperty("created")
  void setCreated(Boolean created);

  @JsonProperty("endUserId")
  String getEndUserId();

  @JsonProperty("endUserId")
  void setEndUserId(String endUserId);
}
