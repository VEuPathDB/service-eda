package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = RankedAbundancePluginRequestImpl.class
)
public interface RankedAbundancePluginRequest extends ComputeRequestBase {
  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("filters")
  List<APIFilter> getFilters();

  @JsonProperty("filters")
  void setFilters(List<APIFilter> filters);

  @JsonProperty("derivedVariables")
  List<DerivedVariable> getDerivedVariables();

  @JsonProperty("derivedVariables")
  void setDerivedVariables(List<DerivedVariable> derivedVariables);

  @JsonProperty("config")
  RankedAbundancePluginConfig getConfig();

  @JsonProperty("config")
  void setConfig(RankedAbundancePluginConfig config);
}
