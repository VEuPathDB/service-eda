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
    "var1",
    "var2",
    "correlationCoef",
    "pValue",
    "adjustedPValue"
})
public class CorrelationResultImpl implements CorrelationResult {
  @JsonProperty("var1")
  private String var1;

  @JsonProperty("var2")
  private String var2;

  @JsonProperty("correlationCoef")
  private String correlationCoef;

  @JsonProperty("pValue")
  private String pValue;

  @JsonProperty("adjustedPValue")
  private String adjustedPValue;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("var1")
  public String getVar1() {
    return this.var1;
  }

  @JsonProperty("var1")
  public void setVar1(String var1) {
    this.var1 = var1;
  }

  @JsonProperty("var2")
  public String getVar2() {
    return this.var2;
  }

  @JsonProperty("var2")
  public void setVar2(String var2) {
    this.var2 = var2;
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
