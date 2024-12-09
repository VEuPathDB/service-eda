package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "staffId",
    "user",
    "isOwner"
})
public class StaffImpl implements Staff {
  @JsonProperty("staffId")
  private Long staffId;

  @JsonProperty("user")
  private UserDetails user;

  @JsonProperty("isOwner")
  private Boolean isOwner;

  @JsonProperty("staffId")
  public Long getStaffId() {
    return this.staffId;
  }

  @JsonProperty("staffId")
  public void setStaffId(Long staffId) {
    this.staffId = staffId;
  }

  @JsonProperty("user")
  public UserDetails getUser() {
    return this.user;
  }

  @JsonProperty("user")
  public void setUser(UserDetails user) {
    this.user = user;
  }

  @JsonProperty("isOwner")
  public Boolean getIsOwner() {
    return this.isOwner;
  }

  @JsonProperty("isOwner")
  public void setIsOwner(Boolean isOwner) {
    this.isOwner = isOwner;
  }
}
