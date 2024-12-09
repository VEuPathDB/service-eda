package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = PlotConfigImpl.class
)
public interface PlotConfig {
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
}
