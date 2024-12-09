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
    "variables",
    "partitionsMetadata"
})
public class BipartiteNetworkConfigImpl implements BipartiteNetworkConfig {
  @JsonProperty("variables")
  private List<VariableMapping> variables;

  @JsonProperty("partitionsMetadata")
  private List<String> partitionsMetadata;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("variables")
  public List<VariableMapping> getVariables() {
    return this.variables;
  }

  @JsonProperty("variables")
  public void setVariables(List<VariableMapping> variables) {
    this.variables = variables;
  }

  @JsonProperty("partitionsMetadata")
  public List<String> getPartitionsMetadata() {
    return this.partitionsMetadata;
  }

  @JsonProperty("partitionsMetadata")
  public void setPartitionsMetadata(List<String> partitionsMetadata) {
    this.partitionsMetadata = partitionsMetadata;
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
