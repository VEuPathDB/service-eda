package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = AdvancedSubsetConfigImpl.class
)
public interface AdvancedSubsetConfig {
  @JsonProperty("rootStepKey")
  String getRootStepKey();

  @JsonProperty("rootStepKey")
  void setRootStepKey(String rootStepKey);

  @JsonProperty("steps")
  List<Step> getSteps();

  @JsonProperty("steps")
  void setSteps(List<Step> steps);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
