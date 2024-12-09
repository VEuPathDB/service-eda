package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isStaff",
    "isOwner",
    "perDataset"
})
public class PermissionsGetResponseImpl implements PermissionsGetResponse {
  @JsonProperty(
      value = "isStaff",
      defaultValue = "false"
  )
  private Boolean isStaff;

  @JsonProperty(
      value = "isOwner",
      defaultValue = "false"
  )
  private Boolean isOwner;

  @JsonProperty("perDataset")
  private PermissionsGetResponse.PerDatasetType perDataset;

  @JsonProperty(
      value = "isStaff",
      defaultValue = "false"
  )
  public Boolean getIsStaff() {
    return this.isStaff;
  }

  @JsonProperty(
      value = "isStaff",
      defaultValue = "false"
  )
  public void setIsStaff(Boolean isStaff) {
    this.isStaff = isStaff;
  }

  @JsonProperty(
      value = "isOwner",
      defaultValue = "false"
  )
  public Boolean getIsOwner() {
    return this.isOwner;
  }

  @JsonProperty(
      value = "isOwner",
      defaultValue = "false"
  )
  public void setIsOwner(Boolean isOwner) {
    this.isOwner = isOwner;
  }

  @JsonProperty("perDataset")
  public PermissionsGetResponse.PerDatasetType getPerDataset() {
    return this.perDataset;
  }

  @JsonProperty("perDataset")
  public void setPerDataset(PermissionsGetResponse.PerDatasetType perDataset) {
    this.perDataset = perDataset;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonPropertyOrder
  public static class PerDatasetTypeImpl implements PermissionsGetResponse.PerDatasetType {
  }
}
