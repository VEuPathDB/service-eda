package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "config"
})
public class KPartiteNetworkImpl implements KPartiteNetwork {
  @JsonProperty("data")
  private KPartiteNetworkData data;

  @JsonProperty("config")
  private KPartiteNetworkConfig config;

  @JsonProperty("data")
  public KPartiteNetworkData getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(KPartiteNetworkData data) {
    this.data = data;
  }

  @JsonProperty("config")
  public KPartiteNetworkConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(KPartiteNetworkConfig config) {
    this.config = config;
  }
}
