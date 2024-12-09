package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationInputDataImpl.class
)
public interface CorrelationInputData {
  @JsonProperty("dataType")
  CorrelationInputDataTypes getDataType();

  @JsonProperty("dataType")
  void setDataType(CorrelationInputDataTypes dataType);

  @JsonProperty("collectionSpec")
  CollectionSpec getCollectionSpec();

  @JsonProperty("collectionSpec")
  void setCollectionSpec(CollectionSpec collectionSpec);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
