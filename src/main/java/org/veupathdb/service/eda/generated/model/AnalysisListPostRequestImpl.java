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
    "notes",
    "descriptor"
})
public class AnalysisListPostRequestImpl implements AnalysisListPostRequest {
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