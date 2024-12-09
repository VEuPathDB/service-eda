package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ExampleComputeVizPostResponseImpl.class
)
public interface ExampleComputeVizPostResponse {
  @JsonProperty("countPluginResult")
  Integer getCountPluginResult();

  @JsonProperty("countPluginResult")
  void setCountPluginResult(Integer countPluginResult);

  @JsonProperty("numEmptyValues")
  Integer getNumEmptyValues();

  @JsonProperty("numEmptyValues")
  void setNumEmptyValues(Integer numEmptyValues);

  @JsonProperty("longestConcatenatedValue")
  String getLongestConcatenatedValue();

  @JsonProperty("longestConcatenatedValue")
  void setLongestConcatenatedValue(String longestConcatenatedValue);

  @JsonProperty("avgConcatenatedLength")
  Number getAvgConcatenatedLength();

  @JsonProperty("avgConcatenatedLength")
  void setAvgConcatenatedLength(Number avgConcatenatedLength);
}
