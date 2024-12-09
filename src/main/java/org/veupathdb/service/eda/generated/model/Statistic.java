package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StatisticImpl.class
)
public interface Statistic {
  @JsonProperty("value")
  Number getValue();

  @JsonProperty("value")
  void setValue(Number value);

  @JsonProperty("confidenceInterval")
  String getConfidenceInterval();

  @JsonProperty("confidenceInterval")
  void setConfidenceInterval(String confidenceInterval);

  @JsonProperty("confidenceLevel")
  Number getConfidenceLevel();

  @JsonProperty("confidenceLevel")
  void setConfidenceLevel(Number confidenceLevel);

  @JsonProperty("pvalue")
  String getPvalue();

  @JsonProperty("pvalue")
  void setPvalue(String pvalue);
}
