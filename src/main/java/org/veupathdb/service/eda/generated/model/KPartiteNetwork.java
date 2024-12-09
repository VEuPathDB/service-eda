package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = KPartiteNetworkImpl.class
)
public interface KPartiteNetwork {
  @JsonProperty("data")
  KPartiteNetworkData getData();

  @JsonProperty("data")
  void setData(KPartiteNetworkData data);

  @JsonProperty("config")
  KPartiteNetworkConfig getConfig();

  @JsonProperty("config")
  void setConfig(KPartiteNetworkConfig config);
}
