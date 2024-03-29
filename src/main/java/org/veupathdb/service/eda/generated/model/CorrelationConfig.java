package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationConfigImpl.class
)
public interface CorrelationConfig extends BaseCorrelationComputeConfig {
  @JsonProperty("correlationMethod")
  CorrelationMethod getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(CorrelationMethod correlationMethod);

  @JsonProperty("prefilterThresholds")
  FeaturePrefilterThresholds getPrefilterThresholds();

  @JsonProperty("prefilterThresholds")
  void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds);

  @JsonProperty("data1")
  CorrelationInputData getData1();

  @JsonProperty("data1")
  void setData1(CorrelationInputData data1);

  @JsonProperty("data2")
  CorrelationInputData getData2();

  @JsonProperty("data2")
  void setData2(CorrelationInputData data2);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
