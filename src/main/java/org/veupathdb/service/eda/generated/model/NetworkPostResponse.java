package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = NetworkPostResponseImpl.class
)
public interface NetworkPostResponse {
  @JsonProperty("network")
  Network getNetwork();

  @JsonProperty("network")
  void setNetwork(Network network);
}
