package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName("continuous")
@JsonDeserialize(
    as = ContinuousAggregationConfigImpl.class
)
public interface ContinuousAggregationConfig extends QuantitativeAggregationConfig {
  OverlayType _DISCRIMINATOR_TYPE_NAME = OverlayType.CONTINUOUS;

  @JsonProperty("overlayType")
  OverlayType getOverlayType();

  @JsonProperty("aggregator")
  Aggregator getAggregator();

  @JsonProperty("aggregator")
  void setAggregator(Aggregator aggregator);
}
