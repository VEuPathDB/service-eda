package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = SingleAnalysisPatchRequestImpl.class
)
public interface SingleAnalysisPatchRequest {
  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("notes")
  String getNotes();

  @JsonProperty("notes")
  void setNotes(String notes);

  @JsonProperty("descriptor")
  AnalysisDescriptor getDescriptor();

  @JsonProperty("descriptor")
  void setDescriptor(AnalysisDescriptor descriptor);

  @JsonProperty("isPublic")
  Boolean getIsPublic();

  @JsonProperty("isPublic")
  void setIsPublic(Boolean isPublic);
}
