package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.OffsetDateTime;

@JsonDeserialize(
    as = EndUserImpl.class
)
public interface EndUser {
  @JsonProperty("user")
  UserDetails getUser();

  @JsonProperty("user")
  void setUser(UserDetails user);

  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("startDate")
  OffsetDateTime getStartDate();

  @JsonProperty("startDate")
  void setStartDate(OffsetDateTime startDate);

  @JsonProperty("duration")
  Long getDuration();

  @JsonProperty("duration")
  void setDuration(Long duration);

  @JsonProperty("restrictionLevel")
  RestrictionLevel getRestrictionLevel();

  @JsonProperty("restrictionLevel")
  void setRestrictionLevel(RestrictionLevel restrictionLevel);

  @JsonProperty("purpose")
  String getPurpose();

  @JsonProperty("purpose")
  void setPurpose(String purpose);

  @JsonProperty("researchQuestion")
  String getResearchQuestion();

  @JsonProperty("researchQuestion")
  void setResearchQuestion(String researchQuestion);

  @JsonProperty("analysisPlan")
  String getAnalysisPlan();

  @JsonProperty("analysisPlan")
  void setAnalysisPlan(String analysisPlan);

  @JsonProperty("disseminationPlan")
  String getDisseminationPlan();

  @JsonProperty("disseminationPlan")
  void setDisseminationPlan(String disseminationPlan);

  @JsonProperty("approvalStatus")
  ApprovalStatus getApprovalStatus();

  @JsonProperty("approvalStatus")
  void setApprovalStatus(ApprovalStatus approvalStatus);

  @JsonProperty("denialReason")
  String getDenialReason();

  @JsonProperty("denialReason")
  void setDenialReason(String denialReason);

  @JsonProperty("priorAuth")
  String getPriorAuth();

  @JsonProperty("priorAuth")
  void setPriorAuth(String priorAuth);

  @JsonProperty("allowEdit")
  Boolean getAllowEdit();

  @JsonProperty("allowEdit")
  void setAllowEdit(Boolean allowEdit);
}
