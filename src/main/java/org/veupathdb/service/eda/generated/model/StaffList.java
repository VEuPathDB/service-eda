package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = StaffListImpl.class
)
public interface StaffList {
  @JsonProperty("data")
  List<Staff> getData();

  @JsonProperty("data")
  void setData(List<Staff> data);

  @JsonProperty("rows")
  Long getRows();

  @JsonProperty("rows")
  void setRows(Long rows);

  @JsonProperty("offset")
  Long getOffset();

  @JsonProperty("offset")
  void setOffset(Long offset);

  @JsonProperty("total")
  Long getTotal();

  @JsonProperty("total")
  void setTotal(Long total);
}
