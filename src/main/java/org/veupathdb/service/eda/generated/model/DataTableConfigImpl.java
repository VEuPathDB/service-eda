package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "variables",
    "sorting"
})
public class DataTableConfigImpl implements DataTableConfig {
  @JsonProperty("variables")
  private List<VariableSpec> variables;

  @JsonProperty("sorting")
  private List<SortSpecEntry> sorting;

  @JsonProperty("variables")
  public List<VariableSpec> getVariables() {
    return this.variables;
  }

  @JsonProperty("variables")
  public void setVariables(List<VariableSpec> variables) {
    this.variables = variables;
  }

  @JsonProperty("sorting")
  public List<SortSpecEntry> getSorting() {
    return this.sorting;
  }

  @JsonProperty("sorting")
  public void setSorting(List<SortSpecEntry> sorting) {
    this.sorting = sorting;
  }
}
