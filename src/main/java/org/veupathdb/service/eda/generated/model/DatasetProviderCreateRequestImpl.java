package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "datasetId",
    "userId",
    "email",
    "isManager"
})
public class DatasetProviderCreateRequestImpl implements DatasetProviderCreateRequest {
  @JsonProperty("datasetId")
  private String datasetId;

  @JsonProperty("userId")
  private Long userId;

  @JsonProperty("email")
  private String email;

  @JsonProperty("isManager")
  private Boolean isManager;

  @JsonProperty("datasetId")
  public String getDatasetId() {
    return this.datasetId;
  }

  @JsonProperty("datasetId")
  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  @JsonProperty("userId")
  public Long getUserId() {
    return this.userId;
  }

  @JsonProperty("userId")
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @JsonProperty("email")
  public String getEmail() {
    return this.email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
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
