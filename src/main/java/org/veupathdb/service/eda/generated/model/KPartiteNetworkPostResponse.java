package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = KPartiteNetworkPostResponseImpl.class
)
public interface KPartiteNetworkPostResponse {
  @JsonProperty("kpartitenetwork")
  KPartiteNetwork getKpartitenetwork();

  @JsonProperty("kpartitenetwork")
  void setKpartitenetwork(KPartiteNetwork kpartitenetwork);
}
