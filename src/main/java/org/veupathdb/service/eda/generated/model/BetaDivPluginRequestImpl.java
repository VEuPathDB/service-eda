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
public class BetaDivPluginRequestImpl implements BetaDivPluginRequest {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("filters")
  private List<APIFilter> filters;

  @JsonProperty("derivedVariables")
  private List<DerivedVariable> derivedVariables;

  @JsonProperty("config")
  private BetaDivPluginConfig config;

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
  public List<DerivedVariable> getDerivedVariables() {
    return this.derivedVariables;
  }

  @JsonProperty("derivedVariables")
  public void setDerivedVariables(List<DerivedVariable> derivedVariables) {
    this.derivedVariables = derivedVariables;
  }

  @JsonProperty("config")
  public BetaDivPluginConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(BetaDivPluginConfig config) {
    this.config = config;
  }
}
