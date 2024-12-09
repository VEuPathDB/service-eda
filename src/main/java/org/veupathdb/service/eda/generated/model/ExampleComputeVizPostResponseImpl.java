package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "countPluginResult",
    "numEmptyValues",
    "longestConcatenatedValue",
    "avgConcatenatedLength"
})
public class ExampleComputeVizPostResponseImpl implements ExampleComputeVizPostResponse {
  @JsonProperty("countPluginResult")
  private Integer countPluginResult;

  @JsonProperty("numEmptyValues")
  private Integer numEmptyValues;

  @JsonProperty("longestConcatenatedValue")
  private String longestConcatenatedValue;

  @JsonProperty("avgConcatenatedLength")
  private Number avgConcatenatedLength;

  @JsonProperty("countPluginResult")
  public Integer getCountPluginResult() {
    return this.countPluginResult;
  }

  @JsonProperty("countPluginResult")
  public void setCountPluginResult(Integer countPluginResult) {
    this.countPluginResult = countPluginResult;
  }

  @JsonProperty("numEmptyValues")
  public Integer getNumEmptyValues() {
    return this.numEmptyValues;
  }

  @JsonProperty("numEmptyValues")
  public void setNumEmptyValues(Integer numEmptyValues) {
    this.numEmptyValues = numEmptyValues;
  }

  @JsonProperty("longestConcatenatedValue")
  public String getLongestConcatenatedValue() {
    return this.longestConcatenatedValue;
  }

  @JsonProperty("longestConcatenatedValue")
  public void setLongestConcatenatedValue(String longestConcatenatedValue) {
    this.longestConcatenatedValue = longestConcatenatedValue;
  }

  @JsonProperty("avgConcatenatedLength")
  public Number getAvgConcatenatedLength() {
    return this.avgConcatenatedLength;
  }

  @JsonProperty("avgConcatenatedLength")
  public void setAvgConcatenatedLength(Number avgConcatenatedLength) {
    this.avgConcatenatedLength = avgConcatenatedLength;
  }
}
