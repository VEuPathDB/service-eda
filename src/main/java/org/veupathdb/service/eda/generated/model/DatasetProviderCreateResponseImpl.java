package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "created",
    "providerId"
})
public class DatasetProviderCreateResponseImpl implements DatasetProviderCreateResponse {
  @JsonProperty("created")
  private Boolean created;

  @JsonProperty("providerId")
  private Long providerId;

  @JsonProperty("created")
  public Boolean getCreated() {
    return this.created;
  }

  @JsonProperty("created")
  public void setCreated(Boolean created) {
    this.created = created;
  }

  @JsonProperty("providerId")
  public Long getProviderId() {
    return this.providerId;
  }

  @JsonProperty("providerId")
  public void setProviderId(Long providerId) {
    this.providerId = providerId;
  }
}
