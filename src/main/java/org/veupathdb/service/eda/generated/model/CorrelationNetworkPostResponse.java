package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CorrelationNetworkPostResponseImpl.class
)
public interface CorrelationNetworkPostResponse extends NetworkPostResponse {
  @JsonProperty("network")
  Network getNetwork();

  @JsonProperty("network")
  void setNetwork(Network network);

  @JsonProperty("significanceThreshold")
  Number getSignificanceThreshold();

  @JsonProperty("significanceThreshold")
  void setSignificanceThreshold(Number significanceThreshold);

  @JsonProperty("correlationCoefThreshold")
  Number getCorrelationCoefThreshold();

  @JsonProperty("correlationCoefThreshold")
  void setCorrelationCoefThreshold(Number correlationCoefThreshold);
}
