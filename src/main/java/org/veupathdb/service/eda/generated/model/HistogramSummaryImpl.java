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
    "min",
    "q1",
    "median",
    "mean",
    "q3",
    "max"
})
public class HistogramSummaryImpl implements HistogramSummary {
  @JsonProperty("min")
  private String min;

  @JsonProperty("q1")
  private String q1;

  @JsonProperty("median")
  private String median;

  @JsonProperty("mean")
  private String mean;

  @JsonProperty("q3")
  private String q3;

  @JsonProperty("max")
  private String max;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("min")
  public String getMin() {
    return this.min;
  }

  @JsonProperty("min")
  public void setMin(String min) {
    this.min = min;
  }

  @JsonProperty("q1")
  public String getQ1() {
    return this.q1;
  }

  @JsonProperty("q1")
  public void setQ1(String q1) {
    this.q1 = q1;
  }

  @JsonProperty("median")
  public String getMedian() {
    return this.median;
  }

  @JsonProperty("median")
  public void setMedian(String median) {
    this.median = median;
  }

  @JsonProperty("mean")
  public String getMean() {
    return this.mean;
  }

  @JsonProperty("mean")
  public void setMean(String mean) {
    this.mean = mean;
  }

  @JsonProperty("q3")
  public String getQ3() {
    return this.q3;
  }

  @JsonProperty("q3")
  public void setQ3(String q3) {
    this.q3 = q3;
  }

  @JsonProperty("max")
  public String getMax() {
    return this.max;
  }

  @JsonProperty("max")
  public void setMax(String max) {
    this.max = max;
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
