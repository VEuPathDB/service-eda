package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "numRows",
    "offset"
})
public class APIPagingConfigImpl implements APIPagingConfig {
  @JsonProperty("numRows")
  private Long numRows;

  @JsonProperty("offset")
  private Long offset;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("numRows")
  public Long getNumRows() {
    return this.numRows;
  }

  @JsonProperty("numRows")
  public void setNumRows(Long numRows) {
    this.numRows = numRows;
  }

  @JsonProperty("offset")
  public Long getOffset() {
    return this.offset;
  }

  @JsonProperty("offset")
  public void setOffset(Long offset) {
    this.offset = offset;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
