package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = BipartiteNetworkPostResponseImpl.class
)
public interface BipartiteNetworkPostResponse {
  @JsonProperty("bipartitenetwork")
  BipartiteNetwork getBipartitenetwork();

  @JsonProperty("bipartitenetwork")
  void setBipartitenetwork(BipartiteNetwork bipartitenetwork);
}
