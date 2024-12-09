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
    "prefilterThresholds",
    "correlationMethod",
    "data1"
})
public class SelfCorrelationConfigImpl implements SelfCorrelationConfig {
  @JsonProperty("prefilterThresholds")
  private FeaturePrefilterThresholds prefilterThresholds;

  @JsonProperty("correlationMethod")
  private SelfCorrelationMethod correlationMethod;

  @JsonProperty("data1")
  private CollectionSpec data1;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("prefilterThresholds")
  public FeaturePrefilterThresholds getPrefilterThresholds() {
    return this.prefilterThresholds;
  }

  @JsonProperty("prefilterThresholds")
  public void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds) {
    this.prefilterThresholds = prefilterThresholds;
  }

  @JsonProperty("correlationMethod")
  public SelfCorrelationMethod getCorrelationMethod() {
    return this.correlationMethod;
  }

  @JsonProperty("correlationMethod")
  public void setCorrelationMethod(SelfCorrelationMethod correlationMethod) {
    this.correlationMethod = correlationMethod;
  }

  @JsonProperty("data1")
  public CollectionSpec getData1() {
    return this.data1;
  }

  @JsonProperty("data1")
  public void setData1(CollectionSpec data1) {
    this.data1 = data1;
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
