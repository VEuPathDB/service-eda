package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "objectsCount",
    "usersCount"
})
public class UsersObjectsCountImpl implements UsersObjectsCount {
  @JsonProperty("objectsCount")
  private Integer objectsCount;

  @JsonProperty("usersCount")
  private Integer usersCount;

  @JsonProperty("objectsCount")
  public Integer getObjectsCount() {
    return this.objectsCount;
  }

  @JsonProperty("objectsCount")
  public void setObjectsCount(Integer objectsCount) {
    this.objectsCount = objectsCount;
  }

  @JsonProperty("usersCount")
  public Integer getUsersCount() {
    return this.usersCount;
  }

  @JsonProperty("usersCount")
  public void setUsersCount(Integer usersCount) {
    this.usersCount = usersCount;
  }
}
