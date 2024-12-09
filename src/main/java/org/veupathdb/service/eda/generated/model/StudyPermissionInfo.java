package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StudyPermissionInfoImpl.class
)
public interface StudyPermissionInfo {
  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("isUserStudy")
  Boolean getIsUserStudy();

  @JsonProperty("isUserStudy")
  void setIsUserStudy(Boolean isUserStudy);

  @JsonProperty("actionAuthorization")
  ActionList getActionAuthorization();

  @JsonProperty("actionAuthorization")
  void setActionAuthorization(ActionList actionAuthorization);
}
