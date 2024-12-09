package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = QuantitativeOverlayConfigImpl.class
)
public interface QuantitativeOverlayConfig {
  @JsonProperty("overlayVariable")
  VariableSpec getOverlayVariable();

  @JsonProperty("overlayVariable")
  void setOverlayVariable(VariableSpec overlayVariable);

  @JsonProperty("aggregationConfig")
  QuantitativeAggregationConfig getAggregationConfig();

  @JsonProperty("aggregationConfig")
  void setAggregationConfig(QuantitativeAggregationConfig aggregationConfig);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
