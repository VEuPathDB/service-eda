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
    "config"
})
public class CollectionFloatingHistogramPostRequestImpl implements CollectionFloatingHistogramPostRequest {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("filters")
  private List<APIFilter> filters;

  @JsonProperty("derivedVariables")
  private List<DerivedVariableSpec> derivedVariables;

  @JsonProperty("config")
  private CollectionFloatingHistogramSpec config;

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

  @JsonProperty("config")
  public CollectionFloatingHistogramSpec getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(CollectionFloatingHistogramSpec config) {
    this.config = config;
  }
}
