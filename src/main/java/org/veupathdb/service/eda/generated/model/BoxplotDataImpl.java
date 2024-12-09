package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overlayVariableDetails",
    "facetVariableDetails",
    "lowerfence",
    "upperfence",
    "q1",
    "q3",
    "median",
    "outliers",
    "rawData",
    "mean",
    "label"
})
public class BoxplotDataImpl implements BoxplotData {
  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("lowerfence")
  private List<Number> lowerfence;

  @JsonProperty("upperfence")
  private List<Number> upperfence;

  @JsonProperty("q1")
  private List<Number> q1;

  @JsonProperty("q3")
  private List<Number> q3;

  @JsonProperty("median")
  private List<Number> median;

  @JsonProperty("outliers")
  private List<List<Number>> outliers;

  @JsonProperty("rawData")
  private List<List<Number>> rawData;

  @JsonProperty("mean")
  private List<Number> mean;

  @JsonProperty("label")
  private List<String> label;

  @JsonProperty("overlayVariableDetails")
  public StrataVariableDetails getOverlayVariableDetails() {
    return this.overlayVariableDetails;
  }

  @JsonProperty("overlayVariableDetails")
  public void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails) {
    this.overlayVariableDetails = overlayVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public List<StrataVariableDetails> getFacetVariableDetails() {
    return this.facetVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails) {
    this.facetVariableDetails = facetVariableDetails;
  }

  @JsonProperty("lowerfence")
  public List<Number> getLowerfence() {
    return this.lowerfence;
  }

  @JsonProperty("lowerfence")
  public void setLowerfence(List<Number> lowerfence) {
    this.lowerfence = lowerfence;
  }

  @JsonProperty("upperfence")
  public List<Number> getUpperfence() {
    return this.upperfence;
  }

  @JsonProperty("upperfence")
  public void setUpperfence(List<Number> upperfence) {
    this.upperfence = upperfence;
  }

  @JsonProperty("q1")
  public List<Number> getQ1() {
    return this.q1;
  }

  @JsonProperty("q1")
  public void setQ1(List<Number> q1) {
    this.q1 = q1;
  }

  @JsonProperty("q3")
  public List<Number> getQ3() {
    return this.q3;
  }

  @JsonProperty("q3")
  public void setQ3(List<Number> q3) {
    this.q3 = q3;
  }

  @JsonProperty("median")
  public List<Number> getMedian() {
    return this.median;
  }

  @JsonProperty("median")
  public void setMedian(List<Number> median) {
    this.median = median;
  }

  @JsonProperty("outliers")
  public List<List<Number>> getOutliers() {
    return this.outliers;
  }

  @JsonProperty("outliers")
  public void setOutliers(List<List<Number>> outliers) {
    this.outliers = outliers;
  }

  @JsonProperty("rawData")
  public List<List<Number>> getRawData() {
    return this.rawData;
  }

  @JsonProperty("rawData")
  public void setRawData(List<List<Number>> rawData) {
    this.rawData = rawData;
  }

  @JsonProperty("mean")
  public List<Number> getMean() {
    return this.mean;
  }

  @JsonProperty("mean")
  public void setMean(List<Number> mean) {
    this.mean = mean;
  }

  @JsonProperty("label")
  public List<String> getLabel() {
    return this.label;
  }

  @JsonProperty("label")
  public void setLabel(List<String> label) {
    this.label = label;
  }
}
