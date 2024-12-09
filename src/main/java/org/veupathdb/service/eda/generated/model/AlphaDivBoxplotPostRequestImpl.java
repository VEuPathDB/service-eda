package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyId",
    "filters",
    "derivedVariables",
    "computeConfig",
    "config"
})
public class AlphaDivBoxplotPostRequestImpl implements AlphaDivBoxplotPostRequest {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("filters")
  private List<APIFilter> filters;

  @JsonProperty("derivedVariables")
  private List<DerivedVariableSpec> derivedVariables;

  @JsonProperty("computeConfig")
  private AlphaDivComputeConfig computeConfig;

  @JsonProperty("config")
  private BoxplotWith1ComputeSpec config;

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

  @JsonProperty("derivedVariables")
  public List<DerivedVariableSpec> getDerivedVariables() {
    return this.derivedVariables;
  }

  @JsonProperty("derivedVariables")
  public void setDerivedVariables(List<DerivedVariableSpec> derivedVariables) {
    this.derivedVariables = derivedVariables;
  }

  @JsonProperty("computeConfig")
  public AlphaDivComputeConfig getComputeConfig() {
    return this.computeConfig;
  }

  @JsonProperty("computeConfig")
  public void setComputeConfig(AlphaDivComputeConfig computeConfig) {
    this.computeConfig = computeConfig;
  }

  @JsonProperty("config")
  public BoxplotWith1ComputeSpec getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(BoxplotWith1ComputeSpec config) {
    this.config = config;
  }
}
