package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data1",
    "data2",
    "correlationCoef",
    "pValue",
    "adjustedPValue"
})
public class CorrelationStatsResponseImpl implements CorrelationStatsResponse {
  @JsonProperty("data1")
  private List<String> data1;

  @JsonProperty("data2")
  private List<String> data2;

  @JsonProperty("correlationCoef")
  private List<String> correlationCoef;

  @JsonProperty("pValue")
  private List<String> pValue;

  @JsonProperty("adjustedPValue")
  private List<String> adjustedPValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("data1")
  public List<String> getData1() {
    return this.data1;
  }

  @JsonProperty("data1")
  public void setData1(List<String> data1) {
    this.data1 = data1;
  }

  @JsonProperty("data2")
  public List<String> getData2() {
    return this.data2;
  }

  @JsonProperty("data2")
  public void setData2(List<String> data2) {
    this.data2 = data2;
  }

  @JsonProperty("correlationCoef")
  public List<String> getCorrelationCoef() {
    return this.correlationCoef;
  }

  @JsonProperty("correlationCoef")
  public void setCorrelationCoef(List<String> correlationCoef) {
    this.correlationCoef = correlationCoef;
  }

  @JsonProperty("pValue")
  public List<String> getPValue() {
    return this.pValue;
  }

  @JsonProperty("pValue")
  public void setPValue(List<String> pValue) {
    this.pValue = pValue;
  }

  @JsonProperty("adjustedPValue")
  public List<String> getAdjustedPValue() {
    return this.adjustedPValue;
  }

  @JsonProperty("adjustedPValue")
  public void setAdjustedPValue(List<String> adjustedPValue) {
    this.adjustedPValue = adjustedPValue;
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
