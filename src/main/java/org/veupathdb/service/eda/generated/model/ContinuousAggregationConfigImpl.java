package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("continuous")
@JsonPropertyOrder({
    "overlayType",
    "aggregator"
})
public class ContinuousAggregationConfigImpl implements ContinuousAggregationConfig {
  @JsonProperty("overlayType")
  private final OverlayType overlayType = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("aggregator")
  private Aggregator aggregator;

  @JsonProperty("overlayType")
  public OverlayType getOverlayType() {
    return this.overlayType;
  }

  @JsonProperty("aggregator")
  public Aggregator getAggregator() {
    return this.aggregator;
  }

  @JsonProperty("aggregator")
  public void setAggregator(Aggregator aggregator) {
    this.aggregator = aggregator;
  }
}
