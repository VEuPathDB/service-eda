package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "geoAggregateVariable",
    "quantitativeOverlayConfig"
})
public class OverlayLegendConfigImpl implements OverlayLegendConfig {
  @JsonProperty("geoAggregateVariable")
  private VariableSpec geoAggregateVariable;

  @JsonProperty("quantitativeOverlayConfig")
  private QuantitativeOverlayConfig quantitativeOverlayConfig;

  @JsonProperty("geoAggregateVariable")
  public VariableSpec getGeoAggregateVariable() {
    return this.geoAggregateVariable;
  }

  @JsonProperty("geoAggregateVariable")
  public void setGeoAggregateVariable(VariableSpec geoAggregateVariable) {
    this.geoAggregateVariable = geoAggregateVariable;
  }

  @JsonProperty("quantitativeOverlayConfig")
  public QuantitativeOverlayConfig getQuantitativeOverlayConfig() {
    return this.quantitativeOverlayConfig;
  }

  @JsonProperty("quantitativeOverlayConfig")
  public void setQuantitativeOverlayConfig(QuantitativeOverlayConfig quantitativeOverlayConfig) {
    this.quantitativeOverlayConfig = quantitativeOverlayConfig;
  }
}
