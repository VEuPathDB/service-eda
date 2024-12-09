package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CollectionMemberAggregateImpl.class
)
public interface CollectionMemberAggregate {
  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("value")
  Number getValue();

  @JsonProperty("value")
  void setValue(Number value);

  @JsonProperty("confidenceInterval")
  NumberRange getConfidenceInterval();

  @JsonProperty("confidenceInterval")
  void setConfidenceInterval(NumberRange confidenceInterval);

  @JsonProperty("n")
  Number getN();

  @JsonProperty("n")
  void setN(Number n);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
