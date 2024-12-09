package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = SubsetMembershipConfigImpl.class
)
public interface SubsetMembershipConfig {
  @JsonProperty("subsetFilters")
  List<APIFilter> getSubsetFilters();

  @JsonProperty("subsetFilters")
  void setSubsetFilters(List<APIFilter> subsetFilters);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
