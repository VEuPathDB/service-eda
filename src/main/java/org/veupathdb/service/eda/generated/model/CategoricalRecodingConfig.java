package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = CategoricalRecodingConfigImpl.class
)
public interface CategoricalRecodingConfig {
  @JsonProperty("inputVariable")
  VariableSpec getInputVariable();

  @JsonProperty("inputVariable")
  void setInputVariable(VariableSpec inputVariable);

  @JsonProperty("rules")
  List<CategoricalRecodingRule> getRules();

  @JsonProperty("rules")
  void setRules(List<CategoricalRecodingRule> rules);

  @JsonProperty("unmappedValue")
  String getUnmappedValue();

  @JsonProperty("unmappedValue")
  void setUnmappedValue(String unmappedValue);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
