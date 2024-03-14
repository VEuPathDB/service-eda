package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ComputeSpecForMergingImpl.class
)
public interface ComputeSpecForMerging {
  @JsonProperty("computeName")
  String getComputeName();

  @JsonProperty("computeName")
  void setComputeName(String computeName);

  @JsonProperty("computeConfig")
  Object getComputeConfig();

  @JsonProperty("computeConfig")
  void setComputeConfig(Object computeConfig);
}
