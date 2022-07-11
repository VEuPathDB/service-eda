package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = ExamplePluginConfigImpl.class
)
public interface ExamplePluginConfig {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
