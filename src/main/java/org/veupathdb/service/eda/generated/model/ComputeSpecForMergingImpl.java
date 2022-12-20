package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "computeName",
    "computeConfig"
})
public class ComputeSpecForMergingImpl implements ComputeSpecForMerging {
  @JsonProperty("computeName")
  private String computeName;

  @JsonProperty("computeConfig")
  private ComputeConfigBase computeConfig;

  @JsonProperty("computeName")
  public String getComputeName() {
    return this.computeName;
  }

  @JsonProperty("computeName")
  public void setComputeName(String computeName) {
    this.computeName = computeName;
  }

  @JsonProperty("computeConfig")
  public ComputeConfigBase getComputeConfig() {
    return this.computeConfig;
  }

  @JsonProperty("computeConfig")
  public void setComputeConfig(ComputeConfigBase computeConfig) {
    this.computeConfig = computeConfig;
  }
}
