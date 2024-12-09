package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = AlphaDivBoxplotPostRequestImpl.class
)
public interface AlphaDivBoxplotPostRequest extends DataPluginRequestBase {
  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("filters")
  List<APIFilter> getFilters();

  @JsonProperty("filters")
  void setFilters(List<APIFilter> filters);

  @JsonProperty("derivedVariables")
  List<DerivedVariableSpec> getDerivedVariables();

  @JsonProperty("derivedVariables")
  void setDerivedVariables(List<DerivedVariableSpec> derivedVariables);

  @JsonProperty("computeConfig")
  AlphaDivComputeConfig getComputeConfig();

  @JsonProperty("computeConfig")
  void setComputeConfig(AlphaDivComputeConfig computeConfig);

  @JsonProperty("config")
  BoxplotWith1ComputeSpec getConfig();

  @JsonProperty("config")
  void setConfig(BoxplotWith1ComputeSpec config);
}
