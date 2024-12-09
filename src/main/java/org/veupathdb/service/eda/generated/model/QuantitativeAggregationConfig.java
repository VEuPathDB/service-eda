package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "overlayType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.CategoricalAggregationConfig.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.ContinuousAggregationConfig.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.QuantitativeAggregationConfig.class)
})
@JsonDeserialize(
    as = QuantitativeAggregationConfigImpl.class
)
public interface QuantitativeAggregationConfig {
  OverlayType _DISCRIMINATOR_TYPE_NAME = null;

  @JsonProperty("overlayType")
  OverlayType getOverlayType();
}
