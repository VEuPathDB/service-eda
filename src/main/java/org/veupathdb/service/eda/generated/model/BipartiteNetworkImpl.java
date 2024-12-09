package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "config"
})
public class BipartiteNetworkImpl implements BipartiteNetwork {
  @JsonProperty("data")
  private BipartiteNetworkData data;

  @JsonProperty("config")
  private BipartiteNetworkConfig config;

  @JsonProperty("data")
  public BipartiteNetworkData getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(BipartiteNetworkData data) {
    this.data = data;
  }

  @JsonProperty("config")
  public BipartiteNetworkConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(BipartiteNetworkConfig config) {
    this.config = config;
  }
}
