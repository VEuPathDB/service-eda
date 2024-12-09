package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DatasetProviderCreateRequestImpl.class
)
public interface DatasetProviderCreateRequest {
  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("userId")
  Long getUserId();

  @JsonProperty("userId")
  void setUserId(Long userId);

  @JsonProperty("email")
  String getEmail();

  @JsonProperty("email")
  void setEmail(String email);

  @JsonProperty("isManager")
  Boolean getIsManager();

  @JsonProperty("isManager")
  void setIsManager(Boolean isManager);
}
