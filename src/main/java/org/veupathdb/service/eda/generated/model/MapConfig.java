package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = MapConfigImpl.class
)
public interface MapConfig {
  @JsonProperty("completeCasesGeoVar")
  Number getCompleteCasesGeoVar();

  @JsonProperty("completeCasesGeoVar")
  void setCompleteCasesGeoVar(Number completeCasesGeoVar);
}
