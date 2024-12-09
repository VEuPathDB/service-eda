package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DatasetPermissionEntryImpl.class
)
public interface DatasetPermissionEntry {
  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("sha1Hash")
  String getSha1Hash();

  @JsonProperty("sha1Hash")
  void setSha1Hash(String sha1Hash);

  @JsonProperty("isUserStudy")
  Boolean getIsUserStudy();

  @JsonProperty("isUserStudy")
  void setIsUserStudy(Boolean isUserStudy);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("shortDisplayName")
  String getShortDisplayName();

  @JsonProperty("shortDisplayName")
  void setShortDisplayName(String shortDisplayName);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("type")
  DatasetPermissionLevel getType();

  @JsonProperty("type")
  void setType(DatasetPermissionLevel type);

  @JsonProperty("actionAuthorization")
  ActionList getActionAuthorization();

  @JsonProperty("actionAuthorization")
  void setActionAuthorization(ActionList actionAuthorization);

  @JsonProperty(
      value = "isManager",
      defaultValue = "false"
  )
  Boolean getIsManager();

  @JsonProperty(
      value = "isManager",
      defaultValue = "false"
  )
  void setIsManager(Boolean isManager);

  @JsonProperty("accessRequestStatus")
  ApprovalStatus getAccessRequestStatus();

  @JsonProperty("accessRequestStatus")
  void setAccessRequestStatus(ApprovalStatus accessRequestStatus);
}
