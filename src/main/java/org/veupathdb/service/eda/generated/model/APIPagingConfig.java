package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = APIPagingConfigImpl.class
)
public interface APIPagingConfig {
  @JsonProperty("numRows")
  Long getNumRows();

  @JsonProperty("numRows")
  void setNumRows(Long numRows);

  @JsonProperty("offset")
  Long getOffset();

  @JsonProperty("offset")
  void setOffset(Long offset);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
