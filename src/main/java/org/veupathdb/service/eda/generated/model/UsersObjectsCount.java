package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = UsersObjectsCountImpl.class
)
public interface UsersObjectsCount {
  @JsonProperty("objectsCount")
  Integer getObjectsCount();

  @JsonProperty("objectsCount")
  void setObjectsCount(Integer objectsCount);

  @JsonProperty("usersCount")
  Integer getUsersCount();

  @JsonProperty("usersCount")
  void setUsersCount(Integer usersCount);
}
