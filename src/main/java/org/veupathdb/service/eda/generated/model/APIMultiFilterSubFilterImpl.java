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
    "variableId",
    "stringSet"
})
public class APIMultiFilterSubFilterImpl implements APIMultiFilterSubFilter {
  @JsonProperty("variableId")
  private String variableId;

  @JsonProperty("stringSet")
  private List<String> stringSet;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("variableId")
  public String getVariableId() {
    return this.variableId;
  }

  @JsonProperty("variableId")
  public void setVariableId(String variableId) {
    this.variableId = variableId;
  }

  @JsonProperty("stringSet")
  public List<String> getStringSet() {
    return this.stringSet;
  }

  @JsonProperty("stringSet")
  public void setStringSet(List<String> stringSet) {
    this.stringSet = stringSet;
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
