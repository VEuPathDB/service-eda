package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = AnalysisDetailImpl.class
)
public interface AnalysisDetail extends AnalysisSummary {
  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("studyVersion")
  String getStudyVersion();

  @JsonProperty("studyVersion")
  void setStudyVersion(String studyVersion);

  @JsonProperty("apiVersion")
  String getApiVersion();

  @JsonProperty("apiVersion")
  void setApiVersion(String apiVersion);

  @JsonProperty("isPublic")
  Boolean getIsPublic();

  @JsonProperty("isPublic")
  void setIsPublic(Boolean isPublic);

  @JsonProperty("analysisId")
  String getAnalysisId();

  @JsonProperty("analysisId")
  void setAnalysisId(String analysisId);

  @JsonProperty("creationTime")
  String getCreationTime();

  @JsonProperty("creationTime")
  void setCreationTime(String creationTime);

  @JsonProperty("modificationTime")
  String getModificationTime();

  @JsonProperty("modificationTime")
  void setModificationTime(String modificationTime);

  @JsonProperty("numFilters")
  Integer getNumFilters();

  @JsonProperty("numFilters")
  void setNumFilters(Integer numFilters);

  @JsonProperty("numComputations")
  Integer getNumComputations();

  @JsonProperty("numComputations")
  void setNumComputations(Integer numComputations);

  @JsonProperty("numVisualizations")
  Integer getNumVisualizations();

  @JsonProperty("numVisualizations")
  void setNumVisualizations(Integer numVisualizations);

  @JsonProperty("provenance")
  AnalysisProvenance getProvenance();

  @JsonProperty("provenance")
  void setProvenance(AnalysisProvenance provenance);

  @JsonProperty("notes")
  String getNotes();

  @JsonProperty("notes")
  void setNotes(String notes);

  @JsonProperty("descriptor")
  AnalysisDescriptor getDescriptor();

  @JsonProperty("descriptor")
  void setDescriptor(AnalysisDescriptor descriptor);
}