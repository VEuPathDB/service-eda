package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CurrentProvenancePropsImpl.class
)
public interface CurrentProvenanceProps {
  @JsonProperty("isDeleted")
  Boolean getIsDeleted();

  @JsonProperty("isDeleted")
  void setIsDeleted(Boolean isDeleted);

  @JsonProperty("modificationTime")
  String getModificationTime();

  @JsonProperty("modificationTime")
  void setModificationTime(String modificationTime);

  @JsonProperty("isPublic")
  Boolean getIsPublic();

  @JsonProperty("isPublic")
  void setIsPublic(Boolean isPublic);
}
