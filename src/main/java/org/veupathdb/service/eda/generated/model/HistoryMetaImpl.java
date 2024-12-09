package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rows",
    "offset"
})
public class HistoryMetaImpl implements HistoryMeta {
  @JsonProperty("rows")
  private Long rows;

  @JsonProperty("offset")
  private Long offset;

  @JsonProperty("rows")
  public Long getRows() {
    return this.rows;
  }

  @JsonProperty("rows")
  public void setRows(Long rows) {
    this.rows = rows;
  }

  @JsonProperty("offset")
  public Long getOffset() {
    return this.offset;
  }

  @JsonProperty("offset")
  public void setOffset(Long offset) {
    this.offset = offset;
  }
}
