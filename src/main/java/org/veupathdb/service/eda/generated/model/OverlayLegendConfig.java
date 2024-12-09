package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = OverlayLegendConfigImpl.class
)
public interface OverlayLegendConfig {
  @JsonProperty("geoAggregateVariable")
  VariableSpec getGeoAggregateVariable();

  @JsonProperty("geoAggregateVariable")
  void setGeoAggregateVariable(VariableSpec geoAggregateVariable);

  @JsonProperty("quantitativeOverlayConfig")
  QuantitativeOverlayConfig getQuantitativeOverlayConfig();

  @JsonProperty("quantitativeOverlayConfig")
  void setQuantitativeOverlayConfig(QuantitativeOverlayConfig quantitativeOverlayConfig);
}
