package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "displayName",
    "description",
    "studyId",
    "studyVersion",
    "apiVersion",
    "isPublic",
    "analysisId",
    "creationTime",
    "modificationTime",
    "numFilters",
    "numComputations",
    "numVisualizations",
    "provenance",
    "notes",
    "descriptor"
})
public class AnalysisDetailImpl implements AnalysisDetail {
  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("studyVersion")
  private String studyVersion;

  @JsonProperty("apiVersion")
  private String apiVersion;

  @JsonProperty("isPublic")
  private Boolean isPublic;

  @JsonProperty("analysisId")
  private String analysisId;

  @JsonProperty("creationTime")
  private String creationTime;

  @JsonProperty("modificationTime")
  private String modificationTime;

  @JsonProperty("numFilters")
  private Integer numFilters;

  @JsonProperty("numComputations")
  private Integer numComputations;

  @JsonProperty("numVisualizations")
  private Integer numVisualizations;

  @JsonProperty("provenance")
  private AnalysisProvenance provenance;

  @JsonProperty("notes")
  private String notes;

  @JsonProperty("descriptor")
  private AnalysisDescriptor descriptor;

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("studyId")
  public String getStudyId() {
    return this.studyId;
  }

  @JsonProperty("studyId")
  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  @JsonProperty("studyVersion")
  public String getStudyVersion() {
    return this.studyVersion;
  }

  @JsonProperty("studyVersion")
  public void setStudyVersion(String studyVersion) {
    this.studyVersion = studyVersion;
  }

  @JsonProperty("apiVersion")
  public String getApiVersion() {
    return this.apiVersion;
  }

  @JsonProperty("apiVersion")
  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return this.isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  @JsonProperty("analysisId")
  public String getAnalysisId() {
    return this.analysisId;
  }

  @JsonProperty("analysisId")
  public void setAnalysisId(String analysisId) {
    this.analysisId = analysisId;
  }

  @JsonProperty("creationTime")
  public String getCreationTime() {
    return this.creationTime;
  }

  @JsonProperty("creationTime")
  public void setCreationTime(String creationTime) {
    this.creationTime = creationTime;
  }

  @JsonProperty("modificationTime")
  public String getModificationTime() {
    return this.modificationTime;
  }

  @JsonProperty("modificationTime")
  public void setModificationTime(String modificationTime) {
    this.modificationTime = modificationTime;
  }

  @JsonProperty("numFilters")
  public Integer getNumFilters() {
    return this.numFilters;
  }

  @JsonProperty("numFilters")
  public void setNumFilters(Integer numFilters) {
    this.numFilters = numFilters;
  }

  @JsonProperty("numComputations")
  public Integer getNumComputations() {
    return this.numComputations;
  }

  @JsonProperty("numComputations")
  public void setNumComputations(Integer numComputations) {
    this.numComputations = numComputations;
  }

  @JsonProperty("numVisualizations")
  public Integer getNumVisualizations() {
    return this.numVisualizations;
  }

  @JsonProperty("numVisualizations")
  public void setNumVisualizations(Integer numVisualizations) {
    this.numVisualizations = numVisualizations;
  }

  @JsonProperty("provenance")
  public AnalysisProvenance getProvenance() {
    return this.provenance;
  }

  @JsonProperty("provenance")
  public void setProvenance(AnalysisProvenance provenance) {
    this.provenance = provenance;
  }

  @JsonProperty("notes")
  public String getNotes() {
    return this.notes;
  }

  @JsonProperty("notes")
  public void setNotes(String notes) {
    this.notes = notes;
  }

  @JsonProperty("descriptor")
  public AnalysisDescriptor getDescriptor() {
    return this.descriptor;
  }

  @JsonProperty("descriptor")
  public void setDescriptor(AnalysisDescriptor descriptor) {
    this.descriptor = descriptor;
  }
}
