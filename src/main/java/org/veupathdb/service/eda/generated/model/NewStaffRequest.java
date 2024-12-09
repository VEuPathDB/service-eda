package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = NewStaffRequestImpl.class
)
public interface NewStaffRequest {
  @JsonProperty("userId")
  Long getUserId();

  @JsonProperty("userId")
  void setUserId(Long userId);

  @JsonProperty("isOwner")
  Boolean getIsOwner();

  @JsonProperty("isOwner")
  void setIsOwner(Boolean isOwner);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
