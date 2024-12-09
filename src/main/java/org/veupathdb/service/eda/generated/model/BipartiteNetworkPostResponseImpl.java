package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("bipartitenetwork")
public class BipartiteNetworkPostResponseImpl implements BipartiteNetworkPostResponse {
  @JsonProperty("bipartitenetwork")
  private BipartiteNetwork bipartitenetwork;

  @JsonProperty("bipartitenetwork")
  public BipartiteNetwork getBipartitenetwork() {
    return this.bipartitenetwork;
  }

  @JsonProperty("bipartitenetwork")
  public void setBipartitenetwork(BipartiteNetwork bipartitenetwork) {
    this.bipartitenetwork = bipartitenetwork;
  }
}
