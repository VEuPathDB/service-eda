package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "equalInterval",
    "quantile",
    "standardDeviation"
})
public class AllBinRangesImpl implements AllBinRanges {
  @JsonProperty("equalInterval")
  private List<LabeledRange> equalInterval;

  @JsonProperty("quantile")
  private List<LabeledRange> quantile;

  @JsonProperty("standardDeviation")
  private List<LabeledRange> standardDeviation;

  @JsonProperty("equalInterval")
  public List<LabeledRange> getEqualInterval() {
    return this.equalInterval;
  }

  @JsonProperty("equalInterval")
  public void setEqualInterval(List<LabeledRange> equalInterval) {
    this.equalInterval = equalInterval;
  }

  @JsonProperty("quantile")
  public List<LabeledRange> getQuantile() {
    return this.quantile;
  }

  @JsonProperty("quantile")
  public void setQuantile(List<LabeledRange> quantile) {
    this.quantile = quantile;
  }

  @JsonProperty("standardDeviation")
  public List<LabeledRange> getStandardDeviation() {
    return this.standardDeviation;
  }

  @JsonProperty("standardDeviation")
  public void setStandardDeviation(List<LabeledRange> standardDeviation) {
    this.standardDeviation = standardDeviation;
  }
}
