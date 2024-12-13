package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "endUserID",
    "user",
    "datasetPresenterID",
    "restrictionLevel",
    "approvalStatus",
    "startDate",
    "duration",
    "purpose",
    "researchQuestion",
    "analysisPlan",
    "priorAuth",
    "denialReason",
    "dateDenied",
    "allowSelfEdits"
})
public class HistoryRowImpl implements HistoryRow {
  @JsonProperty("endUserID")
  private Long endUserID;

  @JsonProperty("user")
  private HistoryUser user;

  @JsonProperty("datasetPresenterID")
  private String datasetPresenterID;

  @JsonProperty("restrictionLevel")
  private HistoryRow.RestrictionLevelType restrictionLevel;

  @JsonProperty("approvalStatus")
  private HistoryRow.ApprovalStatusType approvalStatus;


  @JsonProperty("startDate")
  private OffsetDateTime startDate;

  @JsonProperty("duration")
  private Long duration;

  @JsonProperty("purpose")
  private String purpose;

  @JsonProperty("researchQuestion")
  private String researchQuestion;

  @JsonProperty("analysisPlan")
  private String analysisPlan;

  @JsonProperty("priorAuth")
  private String priorAuth;

  @JsonProperty("denialReason")
  private String denialReason;


  @JsonProperty("dateDenied")
  private OffsetDateTime dateDenied;

  @JsonProperty("allowSelfEdits")
  private Boolean allowSelfEdits;

  @JsonProperty("endUserID")
  public Long getEndUserID() {
    return this.endUserID;
  }

  @JsonProperty("endUserID")
  public void setEndUserID(Long endUserID) {
    this.endUserID = endUserID;
  }

  @JsonProperty("user")
  public HistoryUser getUser() {
    return this.user;
  }

  @JsonProperty("user")
  public void setUser(HistoryUser user) {
    this.user = user;
  }

  @JsonProperty("datasetPresenterID")
  public String getDatasetPresenterID() {
    return this.datasetPresenterID;
  }

  @JsonProperty("datasetPresenterID")
  public void setDatasetPresenterID(String datasetPresenterID) {
    this.datasetPresenterID = datasetPresenterID;
  }

  @JsonProperty("restrictionLevel")
  public HistoryRow.RestrictionLevelType getRestrictionLevel() {
    return this.restrictionLevel;
  }

  @JsonProperty("restrictionLevel")
  public void setRestrictionLevel(HistoryRow.RestrictionLevelType restrictionLevel) {
    this.restrictionLevel = restrictionLevel;
  }

  @JsonProperty("approvalStatus")
  public HistoryRow.ApprovalStatusType getApprovalStatus() {
    return this.approvalStatus;
  }

  @JsonProperty("approvalStatus")
  public void setApprovalStatus(HistoryRow.ApprovalStatusType approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  @JsonProperty("startDate")
  public OffsetDateTime getStartDate() {
    return this.startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  @JsonProperty("duration")
  public Long getDuration() {
    return this.duration;
  }

  @JsonProperty("duration")
  public void setDuration(Long duration) {
    this.duration = duration;
  }

  @JsonProperty("purpose")
  public String getPurpose() {
    return this.purpose;
  }

  @JsonProperty("purpose")
  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  @JsonProperty("researchQuestion")
  public String getResearchQuestion() {
    return this.researchQuestion;
  }

  @JsonProperty("researchQuestion")
  public void setResearchQuestion(String researchQuestion) {
    this.researchQuestion = researchQuestion;
  }

  @JsonProperty("analysisPlan")
  public String getAnalysisPlan() {
    return this.analysisPlan;
  }

  @JsonProperty("analysisPlan")
  public void setAnalysisPlan(String analysisPlan) {
    this.analysisPlan = analysisPlan;
  }

  @JsonProperty("priorAuth")
  public String getPriorAuth() {
    return this.priorAuth;
  }

  @JsonProperty("priorAuth")
  public void setPriorAuth(String priorAuth) {
    this.priorAuth = priorAuth;
  }

  @JsonProperty("denialReason")
  public String getDenialReason() {
    return this.denialReason;
  }

  @JsonProperty("denialReason")
  public void setDenialReason(String denialReason) {
    this.denialReason = denialReason;
  }

  @JsonProperty("dateDenied")
  public OffsetDateTime getDateDenied() {
    return this.dateDenied;
  }

  @JsonProperty("dateDenied")
  public void setDateDenied(OffsetDateTime dateDenied) {
    this.dateDenied = dateDenied;
  }

  @JsonProperty("allowSelfEdits")
  public Boolean getAllowSelfEdits() {
    return this.allowSelfEdits;
  }

  @JsonProperty("allowSelfEdits")
  public void setAllowSelfEdits(Boolean allowSelfEdits) {
    this.allowSelfEdits = allowSelfEdits;
  }
}
