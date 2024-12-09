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
    "smoothedMeanX",
    "smoothedMeanY",
    "smoothedMeanSE",
    "smoothedMeanError",
    "bestFitLineX",
    "bestFitLineY",
    "r2"
})
public class ScatterplotDataImpl implements ScatterplotData {
  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("seriesY")
  private List<String> seriesY;

  @JsonProperty("seriesX")
  private List<String> seriesX;

  @JsonProperty("smoothedMeanX")
  private List<String> smoothedMeanX;

  @JsonProperty("smoothedMeanY")
  private List<Number> smoothedMeanY;

  @JsonProperty("smoothedMeanSE")
  private List<Number> smoothedMeanSE;

  @JsonProperty("smoothedMeanError")
  private String smoothedMeanError;

  @JsonProperty("bestFitLineX")
  private List<String> bestFitLineX;

  @JsonProperty("bestFitLineY")
  private List<Number> bestFitLineY;

  @JsonProperty("r2")
  private Number r2;

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

  @JsonProperty("smoothedMeanX")
  public List<String> getSmoothedMeanX() {
    return this.smoothedMeanX;
  }

  @JsonProperty("smoothedMeanX")
  public void setSmoothedMeanX(List<String> smoothedMeanX) {
    this.smoothedMeanX = smoothedMeanX;
  }

  @JsonProperty("smoothedMeanY")
  public List<Number> getSmoothedMeanY() {
    return this.smoothedMeanY;
  }

  @JsonProperty("smoothedMeanY")
  public void setSmoothedMeanY(List<Number> smoothedMeanY) {
    this.smoothedMeanY = smoothedMeanY;
  }

  @JsonProperty("smoothedMeanSE")
  public List<Number> getSmoothedMeanSE() {
    return this.smoothedMeanSE;
  }

  @JsonProperty("smoothedMeanSE")
  public void setSmoothedMeanSE(List<Number> smoothedMeanSE) {
    this.smoothedMeanSE = smoothedMeanSE;
  }

  @JsonProperty("smoothedMeanError")
  public String getSmoothedMeanError() {
    return this.smoothedMeanError;
  }

  @JsonProperty("smoothedMeanError")
  public void setSmoothedMeanError(String smoothedMeanError) {
    this.smoothedMeanError = smoothedMeanError;
  }

  @JsonProperty("bestFitLineX")
  public List<String> getBestFitLineX() {
    return this.bestFitLineX;
  }

  @JsonProperty("bestFitLineX")
  public void setBestFitLineX(List<String> bestFitLineX) {
    this.bestFitLineX = bestFitLineX;
  }

  @JsonProperty("bestFitLineY")
  public List<Number> getBestFitLineY() {
    return this.bestFitLineY;
  }

  @JsonProperty("bestFitLineY")
  public void setBestFitLineY(List<Number> bestFitLineY) {
    this.bestFitLineY = bestFitLineY;
  }

  @JsonProperty("r2")
  public Number getR2() {
    return this.r2;
  }

  @JsonProperty("r2")
  public void setR2(Number r2) {
    this.r2 = r2;
  }
}
