package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("analysisId")
public class AnalysisListPostResponseImpl implements AnalysisListPostResponse {
  @JsonProperty("analysisId")
  private String analysisId;

  @JsonProperty("analysisId")
  public String getAnalysisId() {
    return this.analysisId;
  }

  @JsonProperty("analysisId")
  public void setAnalysisId(String analysisId) {
    this.analysisId = analysisId;
  }
}
