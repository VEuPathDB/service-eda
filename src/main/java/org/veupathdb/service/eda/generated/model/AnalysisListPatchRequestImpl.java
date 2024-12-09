package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "analysisIdsToDelete",
    "inheritOwnershipFrom"
})
public class AnalysisListPatchRequestImpl implements AnalysisListPatchRequest {
  @JsonProperty("analysisIdsToDelete")
  private List<String> analysisIdsToDelete;

  @JsonProperty("inheritOwnershipFrom")
  private Long inheritOwnershipFrom;

  @JsonProperty("analysisIdsToDelete")
  public List<String> getAnalysisIdsToDelete() {
    return this.analysisIdsToDelete;
  }

  @JsonProperty("analysisIdsToDelete")
  public void setAnalysisIdsToDelete(List<String> analysisIdsToDelete) {
    this.analysisIdsToDelete = analysisIdsToDelete;
  }

  @JsonProperty("inheritOwnershipFrom")
  public Long getInheritOwnershipFrom() {
    return this.inheritOwnershipFrom;
  }

  @JsonProperty("inheritOwnershipFrom")
  public void setInheritOwnershipFrom(Long inheritOwnershipFrom) {
    this.inheritOwnershipFrom = inheritOwnershipFrom;
  }
}
