package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "displayName",
    "description",
    "notes",
    "descriptor",
    "isPublic"
})
public class SingleAnalysisPatchRequestImpl implements SingleAnalysisPatchRequest {
  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("notes")
  private String notes;

  @JsonProperty("descriptor")
  private AnalysisDescriptor descriptor;

  @JsonProperty("isPublic")
  private Boolean isPublic;

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

  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return this.isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }
}
