package org.veupathdb.service.access.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "user",
    "startDate",
    "duration",
    "restrictionLevel",
    "purpose",
    "researchQuestion",
    "analysisPlan",
    "disseminationPlan",
    "approvalStatus",
    "denialReason"
})
public class EndUserImpl implements EndUser {
  @JsonProperty("user")
  private UserDetails user;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
      using = TimestampDeserializer.class
  )
  @JsonProperty("startDate")
  private Date startDate;

  @JsonProperty("duration")
  private int duration;

  @JsonProperty("restrictionLevel")
  private RestrictionLevel restrictionLevel;

  @JsonProperty("purpose")
  private String purpose;

  @JsonProperty("researchQuestion")
  private String researchQuestion;

  @JsonProperty("analysisPlan")
  private String analysisPlan;

  @JsonProperty("disseminationPlan")
  private String disseminationPlan;

  @JsonProperty("approvalStatus")
  private ApprovalStatus approvalStatus;

  @JsonProperty("denialReason")
  private String denialReason;

  @JsonProperty("user")
  public UserDetails getUser() {
    return this.user;
  }

  @JsonProperty("user")
  public void setUser(UserDetails user) {
    this.user = user;
  }

  @JsonProperty("startDate")
  public Date getStartDate() {
    return this.startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @JsonProperty("duration")
  public int getDuration() {
    return this.duration;
  }

  @JsonProperty("duration")
  public void setDuration(int duration) {
    this.duration = duration;
  }

  @JsonProperty("restrictionLevel")
  public RestrictionLevel getRestrictionLevel() {
    return this.restrictionLevel;
  }

  @JsonProperty("restrictionLevel")
  public void setRestrictionLevel(RestrictionLevel restrictionLevel) {
    this.restrictionLevel = restrictionLevel;
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

  @JsonProperty("disseminationPlan")
  public String getDisseminationPlan() {
    return this.disseminationPlan;
  }

  @JsonProperty("disseminationPlan")
  public void setDisseminationPlan(String disseminationPlan) {
    this.disseminationPlan = disseminationPlan;
  }

  @JsonProperty("approvalStatus")
  public ApprovalStatus getApprovalStatus() {
    return this.approvalStatus;
  }

  @JsonProperty("approvalStatus")
  public void setApprovalStatus(ApprovalStatus approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  @JsonProperty("denialReason")
  public String getDenialReason() {
    return this.denialReason;
  }

  @JsonProperty("denialReason")
  public void setDenialReason(String denialReason) {
    this.denialReason = denialReason;
  }
}
