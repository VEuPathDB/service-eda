package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "rows",
    "offset",
    "total"
})
public class StaffListImpl implements StaffList {
  @JsonProperty("data")
  private List<Staff> data;

  @JsonProperty("rows")
  private Long rows;

  @JsonProperty("offset")
  private Long offset;

  @JsonProperty("total")
  private Long total;

  @JsonProperty("data")
  public List<Staff> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<Staff> data) {
    this.data = data;
  }

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

  @JsonProperty("total")
  public Long getTotal() {
    return this.total;
  }

  @JsonProperty("total")
  public void setTotal(Long total) {
    this.total = total;
  }
}
