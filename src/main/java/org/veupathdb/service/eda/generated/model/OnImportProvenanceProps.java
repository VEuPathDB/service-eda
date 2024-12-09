package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = OnImportProvenancePropsImpl.class
)
public interface OnImportProvenanceProps {
  @JsonProperty("ownerId")
  Long getOwnerId();

  @JsonProperty("ownerId")
  void setOwnerId(Long ownerId);

  @JsonProperty("ownerName")
  String getOwnerName();

  @JsonProperty("ownerName")
  void setOwnerName(String ownerName);

  @JsonProperty("ownerOrganization")
  String getOwnerOrganization();

  @JsonProperty("ownerOrganization")
  void setOwnerOrganization(String ownerOrganization);

  @JsonProperty("analysisId")
  String getAnalysisId();

  @JsonProperty("analysisId")
  void setAnalysisId(String analysisId);

  @JsonProperty("analysisName")
  String getAnalysisName();

  @JsonProperty("analysisName")
  void setAnalysisName(String analysisName);

  @JsonProperty("creationTime")
  String getCreationTime();

  @JsonProperty("creationTime")
  void setCreationTime(String creationTime);

  @JsonProperty("modificationTime")
  String getModificationTime();

  @JsonProperty("modificationTime")
  void setModificationTime(String modificationTime);

  @JsonProperty("isPublic")
  Boolean getIsPublic();

  @JsonProperty("isPublic")
  void setIsPublic(Boolean isPublic);
}
