package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "min",
    "max",
    "step"
})
public class BinSliderImpl implements BinSlider {
  @JsonProperty("min")
  private Number min;

  @JsonProperty("max")
  private Number max;

  @JsonProperty("step")
  private Number step;

  @JsonProperty("min")
  public Number getMin() {
    return this.min;
  }

  @JsonProperty("min")
  public void setMin(Number min) {
    this.min = min;
  }

  @JsonProperty("max")
  public Number getMax() {
    return this.max;
  }

  @JsonProperty("max")
  public void setMax(Number max) {
    this.max = max;
  }

  @JsonProperty("step")
  public Number getStep() {
    return this.step;
  }

  @JsonProperty("step")
  public void setStep(Number step) {
    this.step = step;
  }
}