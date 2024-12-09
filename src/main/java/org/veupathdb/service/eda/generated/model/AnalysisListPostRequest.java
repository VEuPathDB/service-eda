package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = AnalysisListPostRequestImpl.class
)
public interface AnalysisListPostRequest extends AnalysisBase {
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

  @JsonProperty("notes")
  String getNotes();

  @JsonProperty("notes")
  void setNotes(String notes);

  @JsonProperty("descriptor")
  AnalysisDescriptor getDescriptor();

  @JsonProperty("descriptor")
  void setDescriptor(AnalysisDescriptor descriptor);
}
