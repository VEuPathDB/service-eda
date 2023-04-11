package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "label",
    "min",
    "max"
})
public class LabeledValueRangeImpl implements LabeledValueRange {
  @JsonProperty("label")
  private String label;

  @JsonProperty("min")
  private String min;

  @JsonProperty("max")
  private String max;

  @JsonProperty("label")
  public String getLabel() {
    return this.label;
  }

  @JsonProperty("label")
  public void setLabel(String label) {
    this.label = label;
  }

  @JsonProperty("min")
  public String getMin() {
    return this.min;
  }

  @JsonProperty("min")
  public void setMin(String min) {
    this.min = min;
  }

  @JsonProperty("max")
  public String getMax() {
    return this.max;
  }

  @JsonProperty("max")
  public void setMax(String max) {
    this.max = max;
  }
}
