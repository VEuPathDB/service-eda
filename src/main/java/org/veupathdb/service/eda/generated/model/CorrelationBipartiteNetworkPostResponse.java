package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CorrelationBipartiteNetworkPostResponseImpl.class
)
public interface CorrelationBipartiteNetworkPostResponse extends BipartiteNetworkPostResponse {
  @JsonProperty("bipartitenetwork")
  BipartiteNetwork getBipartitenetwork();

  @JsonProperty("bipartitenetwork")
  void setBipartitenetwork(BipartiteNetwork bipartitenetwork);

  @JsonProperty("significanceThreshold")
  Number getSignificanceThreshold();

  @JsonProperty("significanceThreshold")
  void setSignificanceThreshold(Number significanceThreshold);

  @JsonProperty("correlationCoefThreshold")
  Number getCorrelationCoefThreshold();

  @JsonProperty("correlationCoefThreshold")
  void setCorrelationCoefThreshold(Number correlationCoefThreshold);
}
