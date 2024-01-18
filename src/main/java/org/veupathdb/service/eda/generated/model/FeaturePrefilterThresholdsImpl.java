package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "proportionNonZero",
    "variance",
    "standardDeviation"
})
public class FeaturePrefilterThresholdsImpl implements FeaturePrefilterThresholds {
  @JsonProperty("proportionNonZero")
  private Number proportionNonZero;

  @JsonProperty("variance")
  private Number variance;

  @JsonProperty("standardDeviation")
  private Number standardDeviation;

  @JsonProperty("proportionNonZero")
  public Number getProportionNonZero() {
    return this.proportionNonZero;
  }

  @JsonProperty("proportionNonZero")
  public void setProportionNonZero(Number proportionNonZero) {
    this.proportionNonZero = proportionNonZero;
  }

  @JsonProperty("variance")
  public Number getVariance() {
    return this.variance;
  }

  @JsonProperty("variance")
  public void setVariance(Number variance) {
    this.variance = variance;
  }

  @JsonProperty("standardDeviation")
  public Number getStandardDeviation() {
    return this.standardDeviation;
  }

  @JsonProperty("standardDeviation")
  public void setStandardDeviation(Number standardDeviation) {
    this.standardDeviation = standardDeviation;
  }
}
