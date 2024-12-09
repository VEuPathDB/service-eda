package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyId",
    "datasetId",
    "isUserStudy",
    "actionAuthorization"
})
public class StudyPermissionInfoImpl implements StudyPermissionInfo {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("datasetId")
  private String datasetId;

  @JsonProperty("isUserStudy")
  private Boolean isUserStudy;

  @JsonProperty("actionAuthorization")
  private ActionList actionAuthorization;

  @JsonProperty("studyId")
  public String getStudyId() {
    return this.studyId;
  }

  @JsonProperty("studyId")
  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  @JsonProperty("datasetId")
  public String getDatasetId() {
    return this.datasetId;
  }

  @JsonProperty("datasetId")
  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  @JsonProperty("isUserStudy")
  public Boolean getIsUserStudy() {
    return this.isUserStudy;
  }

  @JsonProperty("isUserStudy")
  public void setIsUserStudy(Boolean isUserStudy) {
    this.isUserStudy = isUserStudy;
  }

  @JsonProperty("actionAuthorization")
  public ActionList getActionAuthorization() {
    return this.actionAuthorization;
  }

  @JsonProperty("actionAuthorization")
  public void setActionAuthorization(ActionList actionAuthorization) {
    this.actionAuthorization = actionAuthorization;
  }
}
