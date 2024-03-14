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
    "data1",
    "data2",
    "correlationCoef",
    "pValue",
    "adjustedPValue"
})
public class CorrelationPointImpl implements CorrelationPoint {
  @JsonProperty("data1")
  private String data1;

  @JsonProperty("data2")
  private String data2;

  @JsonProperty("correlationCoef")
  private String correlationCoef;

  @JsonProperty("pValue")
  private String pValue;

  @JsonProperty("adjustedPValue")
  private String adjustedPValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("data1")
  public String getData1() {
    return this.data1;
  }

  @JsonProperty("data1")
  public void setData1(String data1) {
    this.data1 = data1;
  }

  @JsonProperty("data2")
  public String getData2() {
    return this.data2;
  }

  @JsonProperty("data2")
  public void setData2(String data2) {
    this.data2 = data2;
  }

  @JsonProperty("correlationCoef")
  public String getCorrelationCoef() {
    return this.correlationCoef;
  }

  @JsonProperty("correlationCoef")
  public void setCorrelationCoef(String correlationCoef) {
    this.correlationCoef = correlationCoef;
  }

  @JsonProperty("pValue")
  public String getPValue() {
    return this.pValue;
  }

  @JsonProperty("pValue")
  public void setPValue(String pValue) {
    this.pValue = pValue;
  }

  @JsonProperty("adjustedPValue")
  public String getAdjustedPValue() {
    return this.adjustedPValue;
  }

  @JsonProperty("adjustedPValue")
  public void setAdjustedPValue(String adjustedPValue) {
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
