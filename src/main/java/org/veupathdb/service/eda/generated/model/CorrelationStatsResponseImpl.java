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
    "var1",
    "var2",
    "correlationCoef",
    "pValue",
    "adjustedPValue"
})
public class CorrelationStatsResponseImpl implements CorrelationStatsResponse {
  @JsonProperty("var1")
  private List<String> var1;

  @JsonProperty("var2")
  private List<String> var2;

  @JsonProperty("correlationCoef")
  private List<String> correlationCoef;

  @JsonProperty("pValue")
  private List<String> pValue;

  @JsonProperty("adjustedPValue")
  private List<String> adjustedPValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("var1")
  public List<String> getVar1() {
    return this.var1;
  }

  @JsonProperty("var1")
  public void setVar1(List<String> var1) {
    this.var1 = var1;
  }

  @JsonProperty("var2")
  public List<String> getVar2() {
    return this.var2;
  }

  @JsonProperty("var2")
  public void setVar2(List<String> var2) {
    this.var2 = var2;
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
