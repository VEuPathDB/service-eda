package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = PlotReferenceImpl.class
)
public interface PlotReference {
  @JsonProperty("computedVariableId")
  String getComputedVariableId();

  @JsonProperty("computedVariableId")
  void setComputedVariableId(String computedVariableId);

  @JsonProperty("variablePlotRef")
  String getVariablePlotRef();

  @JsonProperty("variablePlotRef")
  void setVariablePlotRef(String variablePlotRef);
}
