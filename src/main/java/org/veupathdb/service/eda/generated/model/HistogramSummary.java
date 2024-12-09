package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = HistogramSummaryImpl.class
)
public interface HistogramSummary {
  @JsonProperty("min")
  String getMin();

  @JsonProperty("min")
  void setMin(String min);

  @JsonProperty("q1")
  String getQ1();

  @JsonProperty("q1")
  void setQ1(String q1);

  @JsonProperty("median")
  String getMedian();

  @JsonProperty("median")
  void setMedian(String median);

  @JsonProperty("mean")
  String getMean();

  @JsonProperty("mean")
  void setMean(String mean);

  @JsonProperty("q3")
  String getQ3();

  @JsonProperty("q3")
  void setQ3(String q3);

  @JsonProperty("max")
  String getMax();

  @JsonProperty("max")
  void setMax(String max);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
