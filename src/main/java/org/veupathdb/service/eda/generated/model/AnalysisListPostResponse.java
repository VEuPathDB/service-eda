package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = AnalysisListPostResponseImpl.class
)
public interface AnalysisListPostResponse {
  @JsonProperty("analysisId")
  String getAnalysisId();

  @JsonProperty("analysisId")
  void setAnalysisId(String analysisId);
}
