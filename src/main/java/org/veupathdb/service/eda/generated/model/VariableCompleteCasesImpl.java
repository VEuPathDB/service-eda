package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "variableDetails",
    "completeCases"
})
public class VariableCompleteCasesImpl implements VariableCompleteCases {
  @JsonProperty("variableDetails")
  private VariableSpec variableDetails;

  @JsonProperty("completeCases")
  private Number completeCases;

  @JsonProperty("variableDetails")
  public VariableSpec getVariableDetails() {
    return this.variableDetails;
  }

  @JsonProperty("variableDetails")
  public void setVariableDetails(VariableSpec variableDetails) {
    this.variableDetails = variableDetails;
  }

  @JsonProperty("completeCases")
  public Number getCompleteCases() {
    return this.completeCases;
  }

  @JsonProperty("completeCases")
  public void setCompleteCases(Number completeCases) {
    this.completeCases = completeCases;
  }
}
