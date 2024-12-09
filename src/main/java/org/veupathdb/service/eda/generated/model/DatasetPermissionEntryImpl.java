package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyId",
    "sha1Hash",
    "isUserStudy",
    "displayName",
    "shortDisplayName",
    "description",
    "type",
    "actionAuthorization",
    "isManager",
    "accessRequestStatus"
})
public class DatasetPermissionEntryImpl implements DatasetPermissionEntry {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("sha1Hash")
  private String sha1Hash;

  @JsonProperty("isUserStudy")
  private Boolean isUserStudy;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("shortDisplayName")
  private String shortDisplayName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("type")
  private DatasetPermissionLevel type;

  @JsonProperty("actionAuthorization")
  private ActionList actionAuthorization;

  @JsonProperty(
      value = "isManager",
      defaultValue = "false"
  )
  private Boolean isManager;

  @JsonProperty("accessRequestStatus")
  private ApprovalStatus accessRequestStatus;

  @JsonProperty("studyId")
  public String getStudyId() {
    return this.studyId;
  }

  @JsonProperty("studyId")
  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  @JsonProperty("sha1Hash")
  public String getSha1Hash() {
    return this.sha1Hash;
  }

  @JsonProperty("sha1Hash")
  public void setSha1Hash(String sha1Hash) {
    this.sha1Hash = sha1Hash;
  }

  @JsonProperty("isUserStudy")
  public Boolean getIsUserStudy() {
    return this.isUserStudy;
  }

  @JsonProperty("isUserStudy")
  public void setIsUserStudy(Boolean isUserStudy) {
    this.isUserStudy = isUserStudy;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("shortDisplayName")
  public String getShortDisplayName() {
    return this.shortDisplayName;
  }

  @JsonProperty("shortDisplayName")
  public void setShortDisplayName(String shortDisplayName) {
    this.shortDisplayName = shortDisplayName;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("type")
  public DatasetPermissionLevel getType() {
    return this.type;
  }

  @JsonProperty("type")
  public void setType(DatasetPermissionLevel type) {
    this.type = type;
  }

  @JsonProperty("actionAuthorization")
  public ActionList getActionAuthorization() {
    return this.actionAuthorization;
  }

  @JsonProperty("actionAuthorization")
  public void setActionAuthorization(ActionList actionAuthorization) {
    this.actionAuthorization = actionAuthorization;
  }

  @JsonProperty(
      value = "isManager",
      defaultValue = "false"
  )
  public Boolean getIsManager() {
    return this.isManager;
  }

  @JsonProperty(
      value = "isManager",
      defaultValue = "false"
  )
  public void setIsManager(Boolean isManager) {
    this.isManager = isManager;
  }

  @JsonProperty("accessRequestStatus")
  public ApprovalStatus getAccessRequestStatus() {
    return this.accessRequestStatus;
  }

  @JsonProperty("accessRequestStatus")
  public void setAccessRequestStatus(ApprovalStatus accessRequestStatus) {
    this.accessRequestStatus = accessRequestStatus;
  }
}
