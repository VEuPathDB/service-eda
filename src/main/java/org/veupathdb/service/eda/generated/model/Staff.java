package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StaffImpl.class
)
public interface Staff {
  @JsonProperty("staffId")
  Long getStaffId();

  @JsonProperty("staffId")
  void setStaffId(Long staffId);

  @JsonProperty("user")
  UserDetails getUser();

  @JsonProperty("user")
  void setUser(UserDetails user);

  @JsonProperty("isOwner")
  Boolean getIsOwner();

  @JsonProperty("isOwner")
  void setIsOwner(Boolean isOwner);
}
