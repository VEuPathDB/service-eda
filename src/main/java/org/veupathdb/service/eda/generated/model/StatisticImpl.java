package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "value",
    "confidenceInterval",
    "confidenceLevel",
    "pvalue"
})
public class StatisticImpl implements Statistic {
  @JsonProperty("value")
  private Number value;

  @JsonProperty("confidenceInterval")
  private String confidenceInterval;

  @JsonProperty("confidenceLevel")
  private Number confidenceLevel;

  @JsonProperty("pvalue")
  private String pvalue;

  @JsonProperty("value")
  public Number getValue() {
    return this.value;
  }

  @JsonProperty("value")
  public void setValue(Number value) {
    this.value = value;
  }

  @JsonProperty("confidenceInterval")
  public String getConfidenceInterval() {
    return this.confidenceInterval;
  }

  @JsonProperty("confidenceInterval")
  public void setConfidenceInterval(String confidenceInterval) {
    this.confidenceInterval = confidenceInterval;
  }

  @JsonProperty("confidenceLevel")
  public Number getConfidenceLevel() {
    return this.confidenceLevel;
  }

  @JsonProperty("confidenceLevel")
  public void setConfidenceLevel(Number confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
  }

  @JsonProperty("pvalue")
  public String getPvalue() {
    return this.pvalue;
  }

  @JsonProperty("pvalue")
  public void setPvalue(String pvalue) {
    this.pvalue = pvalue;
  }
}
