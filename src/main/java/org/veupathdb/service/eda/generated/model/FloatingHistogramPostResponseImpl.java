package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("histogram")
public class FloatingHistogramPostResponseImpl implements FloatingHistogramPostResponse {
  @JsonProperty("histogram")
  private FloatingHistogram histogram;

  @JsonProperty("histogram")
  public FloatingHistogram getHistogram() {
    return this.histogram;
  }

  @JsonProperty("histogram")
  public void setHistogram(FloatingHistogram histogram) {
    this.histogram = histogram;
  }
}
