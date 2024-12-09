package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyMetadata",
    "subsetting",
    "visualizations",
    "resultsFirstPage",
    "resultsAll"
})
public class ActionListImpl implements ActionList {
  @JsonProperty("studyMetadata")
  private Boolean studyMetadata;

  @JsonProperty("subsetting")
  private Boolean subsetting;

  @JsonProperty("visualizations")
  private Boolean visualizations;

  @JsonProperty("resultsFirstPage")
  private Boolean resultsFirstPage;

  @JsonProperty("resultsAll")
  private Boolean resultsAll;

  @JsonProperty("studyMetadata")
  public Boolean getStudyMetadata() {
    return this.studyMetadata;
  }

  @JsonProperty("studyMetadata")
  public void setStudyMetadata(Boolean studyMetadata) {
    this.studyMetadata = studyMetadata;
  }

  @JsonProperty("subsetting")
  public Boolean getSubsetting() {
    return this.subsetting;
  }

  @JsonProperty("subsetting")
  public void setSubsetting(Boolean subsetting) {
    this.subsetting = subsetting;
  }

  @JsonProperty("visualizations")
  public Boolean getVisualizations() {
    return this.visualizations;
  }

  @JsonProperty("visualizations")
  public void setVisualizations(Boolean visualizations) {
    this.visualizations = visualizations;
  }

  @JsonProperty("resultsFirstPage")
  public Boolean getResultsFirstPage() {
    return this.resultsFirstPage;
  }

  @JsonProperty("resultsFirstPage")
  public void setResultsFirstPage(Boolean resultsFirstPage) {
    this.resultsFirstPage = resultsFirstPage;
  }

  @JsonProperty("resultsAll")
  public Boolean getResultsAll() {
    return this.resultsAll;
  }

  @JsonProperty("resultsAll")
  public void setResultsAll(Boolean resultsAll) {
    this.resultsAll = resultsAll;
  }
}
