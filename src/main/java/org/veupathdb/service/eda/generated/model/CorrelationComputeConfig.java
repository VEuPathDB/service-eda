package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationComputeConfigImpl.class
)
public interface CorrelationComputeConfig {
  @JsonProperty("correlationMethod")
  CorrelationMethod getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(CorrelationMethod correlationMethod);

  @JsonProperty("prefilterThresholds")
  FeaturePrefilterThresholds getPrefilterThresholds();

  @JsonProperty("prefilterThresholds")
  void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
