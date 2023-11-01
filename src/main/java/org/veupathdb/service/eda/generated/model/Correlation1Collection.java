package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = Correlation1CollectionImpl.class
)
public interface Correlation1Collection extends CorrelationComputeConfig {
  @JsonProperty("correlationMethod")
  CorrelationMethod getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(CorrelationMethod correlationMethod);

  @JsonProperty("collectionVariable")
  CollectionSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(CollectionSpec collectionVariable);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
