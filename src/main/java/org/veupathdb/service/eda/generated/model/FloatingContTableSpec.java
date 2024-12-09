package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingContTableSpecImpl.class
)
public interface FloatingContTableSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("yAxisVariable")
  VariableSpec getYAxisVariable();

  @JsonProperty("yAxisVariable")
  void setYAxisVariable(VariableSpec yAxisVariable);
}
