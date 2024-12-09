package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = AnalysisListPatchRequestImpl.class
)
public interface AnalysisListPatchRequest {
  @JsonProperty("analysisIdsToDelete")
  List<String> getAnalysisIdsToDelete();

  @JsonProperty("analysisIdsToDelete")
  void setAnalysisIdsToDelete(List<String> analysisIdsToDelete);

  @JsonProperty("inheritOwnershipFrom")
  Long getInheritOwnershipFrom();

  @JsonProperty("inheritOwnershipFrom")
  void setInheritOwnershipFrom(Long inheritOwnershipFrom);
}
