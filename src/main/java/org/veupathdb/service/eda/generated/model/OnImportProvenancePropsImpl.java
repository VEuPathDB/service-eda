package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ownerId",
    "ownerName",
    "ownerOrganization",
    "analysisId",
    "analysisName",
    "creationTime",
    "modificationTime",
    "isPublic"
})
public class OnImportProvenancePropsImpl implements OnImportProvenanceProps {
  @JsonProperty("ownerId")
  private Long ownerId;

  @JsonProperty("ownerName")
  private String ownerName;

  @JsonProperty("ownerOrganization")
  private String ownerOrganization;

  @JsonProperty("analysisId")
  private String analysisId;

  @JsonProperty("analysisName")
  private String analysisName;

  @JsonProperty("creationTime")
  private String creationTime;

  @JsonProperty("modificationTime")
  private String modificationTime;

  @JsonProperty("isPublic")
  private Boolean isPublic;

  @JsonProperty("ownerId")
  public Long getOwnerId() {
    return this.ownerId;
  }

  @JsonProperty("ownerId")
  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  @JsonProperty("ownerName")
  public String getOwnerName() {
    return this.ownerName;
  }

  @JsonProperty("ownerName")
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  @JsonProperty("ownerOrganization")
  public String getOwnerOrganization() {
    return this.ownerOrganization;
  }

  @JsonProperty("ownerOrganization")
  public void setOwnerOrganization(String ownerOrganization) {
    this.ownerOrganization = ownerOrganization;
  }

  @JsonProperty("analysisId")
  public String getAnalysisId() {
    return this.analysisId;
  }

  @JsonProperty("analysisId")
  public void setAnalysisId(String analysisId) {
    this.analysisId = analysisId;
  }

  @JsonProperty("analysisName")
  public String getAnalysisName() {
    return this.analysisName;
  }

  @JsonProperty("analysisName")
  public void setAnalysisName(String analysisName) {
    this.analysisName = analysisName;
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

  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return this.isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }
}
