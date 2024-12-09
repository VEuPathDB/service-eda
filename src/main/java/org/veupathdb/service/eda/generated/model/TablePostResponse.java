package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = TablePostResponseImpl.class
)
public interface TablePostResponse {
  @JsonProperty("columns")
  List<VariableSpec> getColumns();

  @JsonProperty("columns")
  void setColumns(List<VariableSpec> columns);

  @JsonProperty("rows")
  List<List<String>> getRows();

  @JsonProperty("rows")
  void setRows(List<List<String>> rows);
}
