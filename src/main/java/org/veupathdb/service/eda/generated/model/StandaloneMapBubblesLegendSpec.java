package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StandaloneMapBubblesLegendSpecImpl.class
)
public interface StandaloneMapBubblesLegendSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("colorLegendConfig")
  OverlayLegendConfig getColorLegendConfig();

  @JsonProperty("colorLegendConfig")
  void setColorLegendConfig(OverlayLegendConfig colorLegendConfig);

  @JsonProperty("sizeConfig")
  SizeLegendConfig getSizeConfig();

  @JsonProperty("sizeConfig")
  void setSizeConfig(SizeLegendConfig sizeConfig);
}
