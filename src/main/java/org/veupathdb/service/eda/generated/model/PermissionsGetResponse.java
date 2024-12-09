package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = PermissionsGetResponseImpl.class
)
public interface PermissionsGetResponse {
  @JsonProperty(
      value = "isStaff",
      defaultValue = "false"
  )
  Boolean getIsStaff();

  @JsonProperty(
      value = "isStaff",
      defaultValue = "false"
  )
  void setIsStaff(Boolean isStaff);

  @JsonProperty(
      value = "isOwner",
      defaultValue = "false"
  )
  Boolean getIsOwner();

  @JsonProperty(
      value = "isOwner",
      defaultValue = "false"
  )
  void setIsOwner(Boolean isOwner);

  @JsonProperty("perDataset")
  PerDatasetType getPerDataset();

  @JsonProperty("perDataset")
  void setPerDataset(PerDatasetType perDataset);

  @JsonDeserialize(
      as = PermissionsGetResponseImpl.PerDatasetTypeImpl.class
  )
  interface PerDatasetType {
  }
}
