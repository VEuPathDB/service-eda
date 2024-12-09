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
    "data1",
    "data2"
})
public class CorrelationAssayAssayConfigImpl implements CorrelationAssayAssayConfig {
  @JsonProperty("prefilterThresholds")
  private FeaturePrefilterThresholds prefilterThresholds;

  @JsonProperty("correlationMethod")
  private CorrelationMethod correlationMethod;

  @JsonProperty("data1")
  private CorrelationInputDataCollection data1;

  @JsonProperty("data2")
  private CorrelationInputDataCollection data2;

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
  public CorrelationMethod getCorrelationMethod() {
    return this.correlationMethod;
  }

  @JsonProperty("correlationMethod")
  public void setCorrelationMethod(CorrelationMethod correlationMethod) {
    this.correlationMethod = correlationMethod;
  }

  @JsonProperty("data1")
  public CorrelationInputDataCollection getData1() {
    return this.data1;
  }

  @JsonProperty("data1")
  public void setData1(CorrelationInputDataCollection data1) {
    this.data1 = data1;
  }

  @JsonProperty("data2")
  public CorrelationInputDataCollection getData2() {
    return this.data2;
  }

  @JsonProperty("data2")
  public void setData2(CorrelationInputDataCollection data2) {
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
