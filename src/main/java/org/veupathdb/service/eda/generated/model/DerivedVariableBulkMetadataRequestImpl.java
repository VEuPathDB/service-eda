package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyId",
    "derivedVariables"
})
public class DerivedVariableBulkMetadataRequestImpl implements DerivedVariableBulkMetadataRequest {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("derivedVariables")
  private List<DerivedVariableSpec> derivedVariables;

  @JsonProperty("studyId")
  public String getStudyId() {
    return this.studyId;
  }

  @JsonProperty("studyId")
  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  @JsonProperty("derivedVariables")
  public List<DerivedVariableSpec> getDerivedVariables() {
    return this.derivedVariables;
  }

  @JsonProperty("derivedVariables")
  public void setDerivedVariables(List<DerivedVariableSpec> derivedVariables) {
    this.derivedVariables = derivedVariables;
  }
}
