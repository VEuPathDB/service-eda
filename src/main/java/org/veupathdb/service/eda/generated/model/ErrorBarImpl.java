package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lowerBound",
    "upperBound",
    "error"
})
public class ErrorBarImpl implements ErrorBar {
  @JsonProperty("lowerBound")
  private Number lowerBound;

  @JsonProperty("upperBound")
  private Number upperBound;

  @JsonProperty("error")
  private String error;

  @JsonProperty("lowerBound")
  public Number getLowerBound() {
    return this.lowerBound;
  }

  @JsonProperty("lowerBound")
  public void setLowerBound(Number lowerBound) {
    this.lowerBound = lowerBound;
  }

  @JsonProperty("upperBound")
  public Number getUpperBound() {
    return this.upperBound;
  }

  @JsonProperty("upperBound")
  public void setUpperBound(Number upperBound) {
    this.upperBound = upperBound;
  }

  @JsonProperty("error")
  public String getError() {
    return this.error;
  }

  @JsonProperty("error")
  public void setError(String error) {
    this.error = error;
  }
}
