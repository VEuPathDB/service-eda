package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = CategoricalRecodingRuleImpl.class
)
public interface CategoricalRecodingRule {
  @JsonProperty("inputValues")
  List<String> getInputValues();

  @JsonProperty("inputValues")
  void setInputValues(List<String> inputValues);

  @JsonProperty("outputValue")
  String getOutputValue();

  @JsonProperty("outputValue")
  void setOutputValue(String outputValue);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
