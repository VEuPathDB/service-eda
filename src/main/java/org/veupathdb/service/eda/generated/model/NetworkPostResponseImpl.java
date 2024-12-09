package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("network")
public class NetworkPostResponseImpl implements NetworkPostResponse {
  @JsonProperty("network")
  private Network network;

  @JsonProperty("network")
  public Network getNetwork() {
    return this.network;
  }

  @JsonProperty("network")
  public void setNetwork(Network network) {
    this.network = network;
  }
}
