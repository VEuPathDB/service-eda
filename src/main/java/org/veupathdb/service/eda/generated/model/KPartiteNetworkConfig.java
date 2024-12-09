package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = KPartiteNetworkConfigImpl.class
)
public interface KPartiteNetworkConfig extends NetworkConfig {
  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);

  @JsonProperty("partitionsMetadata")
  List<String> getPartitionsMetadata();

  @JsonProperty("partitionsMetadata")
  void setPartitionsMetadata(List<String> partitionsMetadata);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
