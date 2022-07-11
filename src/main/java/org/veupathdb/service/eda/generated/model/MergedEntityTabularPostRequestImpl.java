package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyId",
    "filters",
    "entityId",
    "outputVariables",
    "derivedVariables"
})
public class MergedEntityTabularPostRequestImpl implements MergedEntityTabularPostRequest {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("filters")
  private List<APIFilter> filters;

  @JsonProperty("entityId")
  private String entityId;

  @JsonProperty("outputVariables")
  private List<VariableSpec> outputVariables;

  @JsonProperty("derivedVariables")
  private List<DerivedVariable> derivedVariables;

  @JsonProperty("studyId")
  public String getStudyId() {
    return this.studyId;
  }

  @JsonProperty("studyId")
  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  @JsonProperty("filters")
  public List<APIFilter> getFilters() {
    return this.filters;
  }

  @JsonProperty("filters")
  public void setFilters(List<APIFilter> filters) {
    this.filters = filters;
  }

  @JsonProperty("entityId")
  public String getEntityId() {
    return this.entityId;
  }

  @JsonProperty("entityId")
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @JsonProperty("outputVariables")
  public List<VariableSpec> getOutputVariables() {
    return this.outputVariables;
  }

  @JsonProperty("outputVariables")
  public void setOutputVariables(List<VariableSpec> outputVariables) {
    this.outputVariables = outputVariables;
  }

  @JsonProperty("derivedVariables")
  public List<DerivedVariable> getDerivedVariables() {
    return this.derivedVariables;
  }

  @JsonProperty("derivedVariables")
  public void setDerivedVariables(List<DerivedVariable> derivedVariables) {
    this.derivedVariables = derivedVariables;
  }
}
