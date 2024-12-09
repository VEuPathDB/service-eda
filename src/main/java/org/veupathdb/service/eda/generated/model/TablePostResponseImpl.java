package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "columns",
    "rows"
})
public class TablePostResponseImpl implements TablePostResponse {
  @JsonProperty("columns")
  private List<VariableSpec> columns;

  @JsonProperty("rows")
  private List<List<String>> rows;

  @JsonProperty("columns")
  public List<VariableSpec> getColumns() {
    return this.columns;
  }

  @JsonProperty("columns")
  public void setColumns(List<VariableSpec> columns) {
    this.columns = columns;
  }

  @JsonProperty("rows")
  public List<List<String>> getRows() {
    return this.rows;
  }

  @JsonProperty("rows")
  public void setRows(List<List<String>> rows) {
    this.rows = rows;
  }
}
