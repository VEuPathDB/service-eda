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

  @JsonProperty("collectionVariable")
  CollectionSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(CollectionSpec collectionVariable);

  @JsonProperty("collectionVariable2")
  CollectionSpec getCollectionVariable2();

  @JsonProperty("collectionVariable2")
  void setCollectionVariable2(CollectionSpec collectionVariable2);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
