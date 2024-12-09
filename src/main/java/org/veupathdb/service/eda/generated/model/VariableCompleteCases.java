package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = VariableCompleteCasesImpl.class
)
public interface VariableCompleteCases {
  @JsonProperty("variableDetails")
  VariableSpec getVariableDetails();

  @JsonProperty("variableDetails")
  void setVariableDetails(VariableSpec variableDetails);

  @JsonProperty("completeCases")
  Number getCompleteCases();

  @JsonProperty("completeCases")
  void setCompleteCases(Number completeCases);
}
