package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ErrorBarImpl.class
)
public interface ErrorBar {
  @JsonProperty("lowerBound")
  Number getLowerBound();

  @JsonProperty("lowerBound")
  void setLowerBound(Number lowerBound);

  @JsonProperty("upperBound")
  Number getUpperBound();

  @JsonProperty("upperBound")
  void setUpperBound(Number upperBound);

  @JsonProperty("error")
  String getError();

  @JsonProperty("error")
  void setError(String error);
}
