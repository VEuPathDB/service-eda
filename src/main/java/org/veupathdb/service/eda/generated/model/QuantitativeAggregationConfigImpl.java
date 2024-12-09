package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("overlayType")
public class QuantitativeAggregationConfigImpl implements QuantitativeAggregationConfig {
  @JsonProperty("overlayType")
  private final OverlayType overlayType = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("overlayType")
  public OverlayType getOverlayType() {
    return this.overlayType;
  }
}
