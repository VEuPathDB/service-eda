package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overlayVariableDetails",
    "facetVariableDetails",
    "seriesY",
    "seriesX",
    "binStart",
    "binEnd",
    "binSampleSize",
    "errorBars"
})
public class LineplotDataImpl implements LineplotData {
  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("seriesY")
  private List<String> seriesY;

  @JsonProperty("seriesX")
  private List<String> seriesX;

  @JsonProperty("binStart")
  private List<String> binStart;

  @JsonProperty("binEnd")
  private List<String> binEnd;

  @JsonProperty("binSampleSize")
  private List<SampleSize> binSampleSize;

  @JsonProperty("errorBars")
  private List<ErrorBar> errorBars;

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

  @JsonProperty("seriesY")
  public List<String> getSeriesY() {
    return this.seriesY;
  }

  @JsonProperty("seriesY")
  public void setSeriesY(List<String> seriesY) {
    this.seriesY = seriesY;
  }

  @JsonProperty("seriesX")
  public List<String> getSeriesX() {
    return this.seriesX;
  }

  @JsonProperty("seriesX")
  public void setSeriesX(List<String> seriesX) {
    this.seriesX = seriesX;
  }

  @JsonProperty("binStart")
  public List<String> getBinStart() {
    return this.binStart;
  }

  @JsonProperty("binStart")
  public void setBinStart(List<String> binStart) {
    this.binStart = binStart;
  }

  @JsonProperty("binEnd")
  public List<String> getBinEnd() {
    return this.binEnd;
  }

  @JsonProperty("binEnd")
  public void setBinEnd(List<String> binEnd) {
    this.binEnd = binEnd;
  }

  @JsonProperty("binSampleSize")
  public List<SampleSize> getBinSampleSize() {
    return this.binSampleSize;
  }

  @JsonProperty("binSampleSize")
  public void setBinSampleSize(List<SampleSize> binSampleSize) {
    this.binSampleSize = binSampleSize;
  }

  @JsonProperty("errorBars")
  public List<ErrorBar> getErrorBars() {
    return this.errorBars;
  }

  @JsonProperty("errorBars")
  public void setErrorBars(List<ErrorBar> errorBars) {
    this.errorBars = errorBars;
  }
}
