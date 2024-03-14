package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "key",
    "direction"
})
public class SortSpecEntryImpl implements SortSpecEntry {
  @JsonProperty("key")
  private String key;

  @JsonProperty("direction")
  private SortDirection direction;

  @JsonProperty("key")
  public String getKey() {
    return this.key;
  }

  @JsonProperty("key")
  public void setKey(String key) {
    this.key = key;
  }

  @JsonProperty("direction")
  public SortDirection getDirection() {
    return this.direction;
  }

  @JsonProperty("direction")
  public void setDirection(SortDirection direction) {
    this.direction = direction;
  }
}
