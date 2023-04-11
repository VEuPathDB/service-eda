package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = LabeledRangeWithValueImpl.class
)
public interface LabeledRangeWithValue extends LabeledRange {
  @JsonProperty("min")
  String getMin();

  @JsonProperty("min")
  void setMin(String min);

  @JsonProperty("max")
  String getMax();

  @JsonProperty("max")
  void setMax(String max);

  @JsonProperty("label")
  String getLabel();

  @JsonProperty("label")
  void setLabel(String label);

  @JsonProperty("value")
  Number getValue();

  @JsonProperty("value")
  void setValue(Number value);
}
