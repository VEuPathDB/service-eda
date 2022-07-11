package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MergedEntityTabularPostRequestImpl.class
)
public interface MergedEntityTabularPostRequest {
  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("filters")
  List<APIFilter> getFilters();

  @JsonProperty("filters")
  void setFilters(List<APIFilter> filters);

  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("outputVariables")
  List<VariableSpec> getOutputVariables();

  @JsonProperty("outputVariables")
  void setOutputVariables(List<VariableSpec> outputVariables);

  @JsonProperty("derivedVariables")
  List<DerivedVariable> getDerivedVariables();

  @JsonProperty("derivedVariables")
  void setDerivedVariables(List<DerivedVariable> derivedVariables);
}
