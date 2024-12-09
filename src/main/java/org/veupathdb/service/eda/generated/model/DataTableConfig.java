package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DataTableConfigImpl.class
)
public interface DataTableConfig {
  @JsonProperty("variables")
  List<VariableSpec> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableSpec> variables);

  @JsonProperty("sorting")
  List<SortSpecEntry> getSorting();

  @JsonProperty("sorting")
  void setSorting(List<SortSpecEntry> sorting);
}
