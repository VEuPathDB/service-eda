package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DerivedVariableBulkMetadataRequestImpl.class
)
public interface DerivedVariableBulkMetadataRequest {
  @JsonProperty("studyId")
  String getStudyId();

  @JsonProperty("studyId")
  void setStudyId(String studyId);

  @JsonProperty("derivedVariables")
  List<DerivedVariableSpec> getDerivedVariables();

  @JsonProperty("derivedVariables")
  void setDerivedVariables(List<DerivedVariableSpec> derivedVariables);
}
