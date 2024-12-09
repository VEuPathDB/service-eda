package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = ContinuousNumericRecodingConfigImpl.class
)
public interface ContinuousNumericRecodingConfig {
  @JsonProperty("inputVariable")
  VariableSpec getInputVariable();

  @JsonProperty("inputVariable")
  void setInputVariable(VariableSpec inputVariable);

  @JsonProperty("rules")
  List<ContinuousNumericRule> getRules();

  @JsonProperty("rules")
  void setRules(List<ContinuousNumericRule> rules);

  @JsonProperty("unmappedValue")
  String getUnmappedValue();

  @JsonProperty("unmappedValue")
  void setUnmappedValue(String unmappedValue);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
