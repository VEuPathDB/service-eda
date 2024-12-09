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
    "overlayVariable",
    "aggregationConfig"
})
public class QuantitativeOverlayConfigImpl implements QuantitativeOverlayConfig {
  @JsonProperty("overlayVariable")
  private VariableSpec overlayVariable;

  @JsonProperty("aggregationConfig")
  private QuantitativeAggregationConfig aggregationConfig;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("overlayVariable")
  public VariableSpec getOverlayVariable() {
    return this.overlayVariable;
  }

  @JsonProperty("overlayVariable")
  public void setOverlayVariable(VariableSpec overlayVariable) {
    this.overlayVariable = overlayVariable;
  }

  @JsonProperty("aggregationConfig")
  public QuantitativeAggregationConfig getAggregationConfig() {
    return this.aggregationConfig;
  }

  @JsonProperty("aggregationConfig")
  public void setAggregationConfig(QuantitativeAggregationConfig aggregationConfig) {
    this.aggregationConfig = aggregationConfig;
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
