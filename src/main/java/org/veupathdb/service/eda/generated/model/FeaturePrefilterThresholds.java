package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FeaturePrefilterThresholdsImpl.class
)
public interface FeaturePrefilterThresholds {
  @JsonProperty("proportionNonZero")
  Number getProportionNonZero();

  @JsonProperty("proportionNonZero")
  void setProportionNonZero(Number proportionNonZero);

  @JsonProperty("variance")
  Number getVariance();

  @JsonProperty("variance")
  void setVariance(Number variance);

  @JsonProperty("standardDeviation")
  Number getStandardDeviation();

  @JsonProperty("standardDeviation")
  void setStandardDeviation(Number standardDeviation);
}
