package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "config"
})
public class NetworkImpl implements Network {
  @JsonProperty("data")
  private NetworkData data;

  @JsonProperty("config")
  private NetworkConfig config;

  @JsonProperty("data")
  public NetworkData getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(NetworkData data) {
    this.data = data;
  }

  @JsonProperty("config")
  public NetworkConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(NetworkConfig config) {
    this.config = config;
  }
}
