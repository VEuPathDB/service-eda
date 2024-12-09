package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.OffsetDateTime;

@JsonDeserialize(
    as = HistoryRowImpl.class
)
public interface HistoryRow {
  @JsonProperty("endUserID")
  Long getEndUserID();

  @JsonProperty("endUserID")
  void setEndUserID(Long endUserID);

  @JsonProperty("user")
  HistoryUser getUser();

  @JsonProperty("user")
  void setUser(HistoryUser user);

  @JsonProperty("datasetPresenterID")
  String getDatasetPresenterID();

  @JsonProperty("datasetPresenterID")
  void setDatasetPresenterID(String datasetPresenterID);

  @JsonProperty("restrictionLevel")
  RestrictionLevelType getRestrictionLevel();

  @JsonProperty("restrictionLevel")
  void setRestrictionLevel(RestrictionLevelType restrictionLevel);

  @JsonProperty("approvalStatus")
  ApprovalStatusType getApprovalStatus();

  @JsonProperty("approvalStatus")
  void setApprovalStatus(ApprovalStatusType approvalStatus);

  @JsonProperty("startDate")
  OffsetDateTime getStartDate();

  @JsonProperty("startDate")
  void setStartDate(OffsetDateTime startDate);

  @JsonProperty("duration")
  Long getDuration();

  @JsonProperty("duration")
  void setDuration(Long duration);

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

  @JsonProperty("priorAuth")
  String getPriorAuth();

  @JsonProperty("priorAuth")
  void setPriorAuth(String priorAuth);

  @JsonProperty("denialReason")
  String getDenialReason();

  @JsonProperty("denialReason")
  void setDenialReason(String denialReason);

  @JsonProperty("dateDenied")
  OffsetDateTime getDateDenied();

  @JsonProperty("dateDenied")
  void setDateDenied(OffsetDateTime dateDenied);

  @JsonProperty("allowSelfEdits")
  Boolean getAllowSelfEdits();

  @JsonProperty("allowSelfEdits")
  void setAllowSelfEdits(Boolean allowSelfEdits);

  enum ApprovalStatusType {
    @JsonProperty("APPROVED")
    APPROVED("APPROVED"),

    @JsonProperty("REQUESTED")
    REQUESTED("REQUESTED"),

    @JsonProperty("DENIED")
    DENIED("DENIED");

    public final String value;

    public String getValue() {
      return this.value;
    }

    ApprovalStatusType(String name) {
      this.value = name;
    }
  }

  enum RestrictionLevelType {
    @JsonProperty("PUBLIC")
    PUBLIC("PUBLIC"),

    @JsonProperty("PRERELEASE")
    PRERELEASE("PRERELEASE"),

    @JsonProperty("PROTECTED")
    PROTECTED("PROTECTED"),

    @JsonProperty("CONTROLLED")
    CONTROLLED("CONTROLLED"),

    @JsonProperty("PRIVATE")
    PRIVATE("PRIVATE");

    public final String value;

    public String getValue() {
      return this.value;
    }

    RestrictionLevelType(String name) {
      this.value = name;
    }
  }
}
