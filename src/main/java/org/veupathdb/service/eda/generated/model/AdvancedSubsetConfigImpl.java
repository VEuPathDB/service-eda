package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rootStepKey",
    "steps"
})
public class AdvancedSubsetConfigImpl implements AdvancedSubsetConfig {
  @JsonProperty("rootStepKey")
  private String rootStepKey;

  @JsonProperty("steps")
  private List<Step> steps;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("rootStepKey")
  public String getRootStepKey() {
    return this.rootStepKey;
  }

  @JsonProperty("rootStepKey")
  public void setRootStepKey(String rootStepKey) {
    this.rootStepKey = rootStepKey;
  }

  @JsonProperty("steps")
  public List<Step> getSteps() {
    return this.steps;
  }

  @JsonProperty("steps")
  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
