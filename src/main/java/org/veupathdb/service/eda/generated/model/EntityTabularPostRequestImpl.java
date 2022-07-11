package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "filters",
    "outputVariableIds",
    "reportConfig"
})
public class EntityTabularPostRequestImpl implements EntityTabularPostRequest {
  @JsonProperty("filters")
  private List<APIFilter> filters;

  @JsonProperty("outputVariableIds")
  private List<String> outputVariableIds;

  @JsonProperty("reportConfig")
  private APITabularReportConfig reportConfig;

  @JsonProperty("filters")
  public List<APIFilter> getFilters() {
    return this.filters;
  }

  @JsonProperty("filters")
  public void setFilters(List<APIFilter> filters) {
    this.filters = filters;
  }

  @JsonProperty("outputVariableIds")
  public List<String> getOutputVariableIds() {
    return this.outputVariableIds;
  }

  @JsonProperty("outputVariableIds")
  public void setOutputVariableIds(List<String> outputVariableIds) {
    this.outputVariableIds = outputVariableIds;
  }

  @JsonProperty("reportConfig")
  public APITabularReportConfig getReportConfig() {
    return this.reportConfig;
  }

  @JsonProperty("reportConfig")
  public void setReportConfig(APITabularReportConfig reportConfig) {
    this.reportConfig = reportConfig;
  }
}
