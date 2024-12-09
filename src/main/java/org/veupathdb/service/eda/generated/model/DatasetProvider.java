package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DatasetProviderImpl.class
)
public interface DatasetProvider {
  @JsonProperty("providerId")
  Long getProviderId();

  @JsonProperty("providerId")
  void setProviderId(Long providerId);

  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("user")
  UserDetails getUser();

  @JsonProperty("user")
  void setUser(UserDetails user);

  @JsonProperty("isManager")
  Boolean getIsManager();

  @JsonProperty("isManager")
  void setIsManager(Boolean isManager);
}
