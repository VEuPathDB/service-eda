package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = DatasetProviderCreateResponseImpl.class
)
public interface DatasetProviderCreateResponse {
  @JsonProperty("created")
  Boolean getCreated();

  @JsonProperty("created")
  void setCreated(Boolean created);

  @JsonProperty("providerId")
  Long getProviderId();

  @JsonProperty("providerId")
  void setProviderId(Long providerId);
}
