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
  private Object computeConfig;

  @JsonProperty("computeName")
  public String getComputeName() {
    return this.computeName;
  }

  @JsonProperty("computeName")
  public void setComputeName(String computeName) {
    this.computeName = computeName;
  }

  @JsonProperty("computeConfig")
  public Object getComputeConfig() {
    return this.computeConfig;
  }

  @JsonProperty("computeConfig")
  public void setComputeConfig(Object computeConfig) {
    this.computeConfig = computeConfig;
  }
}
