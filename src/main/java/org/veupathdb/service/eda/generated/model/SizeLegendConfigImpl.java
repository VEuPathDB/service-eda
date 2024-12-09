package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("geoAggregateVariable")
public class SizeLegendConfigImpl implements SizeLegendConfig {
  @JsonProperty("geoAggregateVariable")
  private VariableSpec geoAggregateVariable;

  @JsonProperty("geoAggregateVariable")
  public VariableSpec getGeoAggregateVariable() {
    return this.geoAggregateVariable;
  }

  @JsonProperty("geoAggregateVariable")
  public void setGeoAggregateVariable(VariableSpec geoAggregateVariable) {
    this.geoAggregateVariable = geoAggregateVariable;
  }
}
