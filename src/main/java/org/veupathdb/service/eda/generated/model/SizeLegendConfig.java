package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = SizeLegendConfigImpl.class
)
public interface SizeLegendConfig {
  @JsonProperty("geoAggregateVariable")
  VariableSpec getGeoAggregateVariable();

  @JsonProperty("geoAggregateVariable")
  void setGeoAggregateVariable(VariableSpec geoAggregateVariable);
}
