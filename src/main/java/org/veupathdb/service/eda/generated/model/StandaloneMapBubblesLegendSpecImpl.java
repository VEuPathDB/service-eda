package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "colorLegendConfig",
    "sizeConfig"
})
public class StandaloneMapBubblesLegendSpecImpl implements StandaloneMapBubblesLegendSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("colorLegendConfig")
  private OverlayLegendConfig colorLegendConfig;

  @JsonProperty("sizeConfig")
  private SizeLegendConfig sizeConfig;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("colorLegendConfig")
  public OverlayLegendConfig getColorLegendConfig() {
    return this.colorLegendConfig;
  }

  @JsonProperty("colorLegendConfig")
  public void setColorLegendConfig(OverlayLegendConfig colorLegendConfig) {
    this.colorLegendConfig = colorLegendConfig;
  }

  @JsonProperty("sizeConfig")
  public SizeLegendConfig getSizeConfig() {
    return this.sizeConfig;
  }

  @JsonProperty("sizeConfig")
  public void setSizeConfig(SizeLegendConfig sizeConfig) {
    this.sizeConfig = sizeConfig;
  }
}
