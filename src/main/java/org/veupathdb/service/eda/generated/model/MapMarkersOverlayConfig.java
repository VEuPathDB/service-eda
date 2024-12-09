package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MapMarkersOverlayConfigImpl.class
)
public interface MapMarkersOverlayConfig extends PlotConfig {
  @JsonProperty("completeCasesAllVars")
  Number getCompleteCasesAllVars();

  @JsonProperty("completeCasesAllVars")
  void setCompleteCasesAllVars(Number completeCasesAllVars);

  @JsonProperty("completeCasesAxesVars")
  Number getCompleteCasesAxesVars();

  @JsonProperty("completeCasesAxesVars")
  void setCompleteCasesAxesVars(Number completeCasesAxesVars);

  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);

  @JsonProperty("viewport")
  GeolocationViewport getViewport();

  @JsonProperty("viewport")
  void setViewport(GeolocationViewport viewport);
}
