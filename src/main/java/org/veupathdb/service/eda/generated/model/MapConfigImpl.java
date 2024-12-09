package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("completeCasesGeoVar")
public class MapConfigImpl implements MapConfig {
  @JsonProperty("completeCasesGeoVar")
  private Number completeCasesGeoVar;

  @JsonProperty("completeCasesGeoVar")
  public Number getCompleteCasesGeoVar() {
    return this.completeCasesGeoVar;
  }

  @JsonProperty("completeCasesGeoVar")
  public void setCompleteCasesGeoVar(Number completeCasesGeoVar) {
    this.completeCasesGeoVar = completeCasesGeoVar;
  }
}
