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
  @JsonProperty("collectionVariable1")
  CollectionSpec getCollectionVariable1();

  @JsonProperty("collectionVariable1")
  void setCollectionVariable1(CollectionSpec collectionVariable1);

  @JsonProperty("collectionVariable2")
  CollectionSpec getCollectionVariable2();

  @JsonProperty("collectionVariable2")
  void setCollectionVariable2(CollectionSpec collectionVariable2);

  @JsonProperty("correlationMethod")
  CorrelationMethod getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(CorrelationMethod correlationMethod);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
