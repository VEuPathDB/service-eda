package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kpartitenetwork")
public class KPartiteNetworkPostResponseImpl implements KPartiteNetworkPostResponse {
  @JsonProperty("kpartitenetwork")
  private KPartiteNetwork kpartitenetwork;

  @JsonProperty("kpartitenetwork")
  public KPartiteNetwork getKpartitenetwork() {
    return this.kpartitenetwork;
  }

  @JsonProperty("kpartitenetwork")
  public void setKpartitenetwork(KPartiteNetwork kpartitenetwork) {
    this.kpartitenetwork = kpartitenetwork;
  }
}
