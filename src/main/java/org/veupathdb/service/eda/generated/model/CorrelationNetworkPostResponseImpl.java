package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "network",
    "significanceThreshold",
    "correlationCoefThreshold"
})
public class CorrelationNetworkPostResponseImpl implements CorrelationNetworkPostResponse {
  @JsonProperty("network")
  private Network network;

  @JsonProperty("significanceThreshold")
  private Number significanceThreshold;

  @JsonProperty("correlationCoefThreshold")
  private Number correlationCoefThreshold;

  @JsonProperty("network")
  public Network getNetwork() {
    return this.network;
  }

  @JsonProperty("network")
  public void setNetwork(Network network) {
    this.network = network;
  }

  @JsonProperty("significanceThreshold")
  public Number getSignificanceThreshold() {
    return this.significanceThreshold;
  }

  @JsonProperty("significanceThreshold")
  public void setSignificanceThreshold(Number significanceThreshold) {
    this.significanceThreshold = significanceThreshold;
  }

  @JsonProperty("correlationCoefThreshold")
  public Number getCorrelationCoefThreshold() {
    return this.correlationCoefThreshold;
  }

  @JsonProperty("correlationCoefThreshold")
  public void setCorrelationCoefThreshold(Number correlationCoefThreshold) {
    this.correlationCoefThreshold = correlationCoefThreshold;
  }
}
