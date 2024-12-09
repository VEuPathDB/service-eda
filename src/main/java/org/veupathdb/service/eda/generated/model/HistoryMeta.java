package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = HistoryMetaImpl.class
)
public interface HistoryMeta {
  @JsonProperty("rows")
  Long getRows();

  @JsonProperty("rows")
  void setRows(Long rows);

  @JsonProperty("offset")
  Long getOffset();

  @JsonProperty("offset")
  void setOffset(Long offset);
}
