package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = NetworkImpl.class
)
public interface Network {
  @JsonProperty("data")
  NetworkData getData();

  @JsonProperty("data")
  void setData(NetworkData data);

  @JsonProperty("config")
  NetworkConfig getConfig();

  @JsonProperty("config")
  void setConfig(NetworkConfig config);
}
