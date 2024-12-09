package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = ContinuousNumericRuleImpl.class
)
public interface ContinuousNumericRule {
  @JsonProperty("minInclusive")
  Number getMinInclusive();

  @JsonProperty("minInclusive")
  void setMinInclusive(Number minInclusive);

  @JsonProperty("maxExclusive")
  Number getMaxExclusive();

  @JsonProperty("maxExclusive")
  void setMaxExclusive(Number maxExclusive);

  @JsonProperty("outputValue")
  String getOutputValue();

  @JsonProperty("outputValue")
  void setOutputValue(String outputValue);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
