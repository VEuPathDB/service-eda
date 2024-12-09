package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "xMin",
    "xMax"
})
public class NumericViewportImpl implements NumericViewport {
  @JsonProperty("xMin")
  private String xMin;

  @JsonProperty("xMax")
  private String xMax;

  @JsonProperty("xMin")
  public String getXMin() {
    return this.xMin;
  }

  @JsonProperty("xMin")
  public void setXMin(String xMin) {
    this.xMin = xMin;
  }

  @JsonProperty("xMax")
  public String getXMax() {
    return this.xMax;
  }

  @JsonProperty("xMax")
  public void setXMax(String xMax) {
    this.xMax = xMax;
  }
}
