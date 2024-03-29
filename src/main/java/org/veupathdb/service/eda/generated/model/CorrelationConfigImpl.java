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
    "correlationMethod",
    "prefilterThresholds",
    "data1",
    "data2"
})
public class CorrelationConfigImpl implements CorrelationConfig {
  @JsonProperty("correlationMethod")
  private CorrelationMethod correlationMethod;

  @JsonProperty("prefilterThresholds")
  private FeaturePrefilterThresholds prefilterThresholds;

  @JsonProperty("data1")
  private CorrelationInputData data1;

  @JsonProperty("data2")
  private CorrelationInputData data2;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("correlationMethod")
  public CorrelationMethod getCorrelationMethod() {
    return this.correlationMethod;
  }

  @JsonProperty("correlationMethod")
  public void setCorrelationMethod(CorrelationMethod correlationMethod) {
    this.correlationMethod = correlationMethod;
  }

  @JsonProperty("prefilterThresholds")
  public FeaturePrefilterThresholds getPrefilterThresholds() {
    return this.prefilterThresholds;
  }

  @JsonProperty("prefilterThresholds")
  public void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds) {
    this.prefilterThresholds = prefilterThresholds;
  }

  @JsonProperty("data1")
  public CorrelationInputData getData1() {
    return this.data1;
  }

  @JsonProperty("data1")
  public void setData1(CorrelationInputData data1) {
    this.data1 = data1;
  }

  @JsonProperty("data2")
  public CorrelationInputData getData2() {
    return this.data2;
  }

  @JsonProperty("data2")
  public void setData2(CorrelationInputData data2) {
    this.data2 = data2;
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
