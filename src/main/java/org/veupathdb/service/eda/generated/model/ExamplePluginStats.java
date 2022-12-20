package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = ExamplePluginStatsImpl.class
)
public interface ExamplePluginStats {
  @JsonProperty("numEmptyValues")
  Integer getNumEmptyValues();

  @JsonProperty("numEmptyValues")
  void setNumEmptyValues(Integer numEmptyValues);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
