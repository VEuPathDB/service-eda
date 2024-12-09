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
@JsonPropertyOrder("subsetFilters")
public class SubsetMembershipConfigImpl implements SubsetMembershipConfig {
  @JsonProperty("subsetFilters")
  private List<APIFilter> subsetFilters;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("subsetFilters")
  public List<APIFilter> getSubsetFilters() {
    return this.subsetFilters;
  }

  @JsonProperty("subsetFilters")
  public void setSubsetFilters(List<APIFilter> subsetFilters) {
    this.subsetFilters = subsetFilters;
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
