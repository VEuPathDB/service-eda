package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ActionListImpl.class
)
public interface ActionList {
  @JsonProperty("studyMetadata")
  Boolean getStudyMetadata();

  @JsonProperty("studyMetadata")
  void setStudyMetadata(Boolean studyMetadata);

  @JsonProperty("subsetting")
  Boolean getSubsetting();

  @JsonProperty("subsetting")
  void setSubsetting(Boolean subsetting);

  @JsonProperty("visualizations")
  Boolean getVisualizations();

  @JsonProperty("visualizations")
  void setVisualizations(Boolean visualizations);

  @JsonProperty("resultsFirstPage")
  Boolean getResultsFirstPage();

  @JsonProperty("resultsFirstPage")
  void setResultsFirstPage(Boolean resultsFirstPage);

  @JsonProperty("resultsAll")
  Boolean getResultsAll();

  @JsonProperty("resultsAll")
  void setResultsAll(Boolean resultsAll);
}
