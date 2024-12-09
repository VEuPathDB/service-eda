package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bipartitenetwork",
    "significanceThreshold",
    "correlationCoefThreshold"
})
public class CorrelationBipartiteNetworkPostResponseImpl implements CorrelationBipartiteNetworkPostResponse {
  @JsonProperty("bipartitenetwork")
  private BipartiteNetwork bipartitenetwork;

  @JsonProperty("significanceThreshold")
  private Number significanceThreshold;

  @JsonProperty("correlationCoefThreshold")
  private Number correlationCoefThreshold;

  @JsonProperty("bipartitenetwork")
  public BipartiteNetwork getBipartitenetwork() {
    return this.bipartitenetwork;
  }

  @JsonProperty("bipartitenetwork")
  public void setBipartitenetwork(BipartiteNetwork bipartitenetwork) {
    this.bipartitenetwork = bipartitenetwork;
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
