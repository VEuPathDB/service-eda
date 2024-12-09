package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "barMode",
    "valueSpec",
    "overlayConfig"
})
public class CollectionFloatingBarplotSpecImpl implements CollectionFloatingBarplotSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("barMode")
  private CollectionFloatingBarplotSpec.BarModeType barMode;

  @JsonProperty("valueSpec")
  private ValueSpec valueSpec;

  @JsonProperty("overlayConfig")
  private CollectionOverlayConfigWithValues overlayConfig;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("barMode")
  public CollectionFloatingBarplotSpec.BarModeType getBarMode() {
    return this.barMode;
  }

  @JsonProperty("barMode")
  public void setBarMode(CollectionFloatingBarplotSpec.BarModeType barMode) {
    this.barMode = barMode;
  }

  @JsonProperty("valueSpec")
  public ValueSpec getValueSpec() {
    return this.valueSpec;
  }

  @JsonProperty("valueSpec")
  public void setValueSpec(ValueSpec valueSpec) {
    this.valueSpec = valueSpec;
  }

  @JsonProperty("overlayConfig")
  public CollectionOverlayConfigWithValues getOverlayConfig() {
    return this.overlayConfig;
  }

  @JsonProperty("overlayConfig")
  public void setOverlayConfig(CollectionOverlayConfigWithValues overlayConfig) {
    this.overlayConfig = overlayConfig;
  }
}
