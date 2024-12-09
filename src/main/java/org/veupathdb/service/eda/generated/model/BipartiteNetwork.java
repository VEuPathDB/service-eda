package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = BipartiteNetworkImpl.class
)
public interface BipartiteNetwork {
  @JsonProperty("data")
  BipartiteNetworkData getData();

  @JsonProperty("data")
  void setData(BipartiteNetworkData data);

  @JsonProperty("config")
  BipartiteNetworkConfig getConfig();

  @JsonProperty("config")
  void setConfig(BipartiteNetworkConfig config);
}
