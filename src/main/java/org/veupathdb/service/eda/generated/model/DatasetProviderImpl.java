package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "providerId",
    "datasetId",
    "user",
    "isManager"
})
public class DatasetProviderImpl implements DatasetProvider {
  @JsonProperty("providerId")
  private Long providerId;

  @JsonProperty("datasetId")
  private String datasetId;

  @JsonProperty("user")
  private UserDetails user;

  @JsonProperty("isManager")
  private Boolean isManager;

  @JsonProperty("providerId")
  public Long getProviderId() {
    return this.providerId;
  }

  @JsonProperty("providerId")
  public void setProviderId(Long providerId) {
    this.providerId = providerId;
  }

  @JsonProperty("datasetId")
  public String getDatasetId() {
    return this.datasetId;
  }

  @JsonProperty("datasetId")
  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  @JsonProperty("user")
  public UserDetails getUser() {
    return this.user;
  }

  @JsonProperty("user")
  public void setUser(UserDetails user) {
    this.user = user;
  }

  @JsonProperty("isManager")
  public Boolean getIsManager() {
    return this.isManager;
  }

  @JsonProperty("isManager")
  public void setIsManager(Boolean isManager) {
    this.isManager = isManager;
  }
}
