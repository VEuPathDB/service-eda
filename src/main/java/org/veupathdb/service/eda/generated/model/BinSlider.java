package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = BinSliderImpl.class
)
public interface BinSlider {
  @JsonProperty("min")
  Number getMin();

  @JsonProperty("min")
  void setMin(Number min);

  @JsonProperty("max")
  Number getMax();

  @JsonProperty("max")
  void setMax(Number max);

  @JsonProperty("step")
  Number getStep();

  @JsonProperty("step")
  void setStep(Number step);
}
