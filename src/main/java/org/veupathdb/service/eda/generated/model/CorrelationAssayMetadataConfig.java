package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationAssayMetadataConfigImpl.class
)
public interface CorrelationAssayMetadataConfig extends BaseCorrelationComputeConfig {
  @JsonProperty("prefilterThresholds")
  FeaturePrefilterThresholds getPrefilterThresholds();

  @JsonProperty("prefilterThresholds")
  void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds);

  @JsonProperty("correlationMethod")
  CorrelationMethod getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(CorrelationMethod correlationMethod);

  @JsonProperty("data1")
  CorrelationInputDataCollection getData1();

  @JsonProperty("data1")
  void setData1(CorrelationInputDataCollection data1);

  @JsonProperty("data2")
  CorrelationInputDataMetadata getData2();

  @JsonProperty("data2")
  void setData2(CorrelationInputDataMetadata data2);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
