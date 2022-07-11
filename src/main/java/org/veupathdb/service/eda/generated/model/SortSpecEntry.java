package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = SortSpecEntryImpl.class
)
public interface SortSpecEntry {
  @JsonProperty("key")
  String getKey();

  @JsonProperty("key")
  void setKey(String key);

  @JsonProperty("direction")
  SortDirection getDirection();

  @JsonProperty("direction")
  void setDirection(SortDirection direction);
}
