package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ScatterplotDataImpl.class
)
public interface ScatterplotData {
  @JsonProperty("overlayVariableDetails")
  StrataVariableDetails getOverlayVariableDetails();

  @JsonProperty("overlayVariableDetails")
  void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails);

  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("seriesY")
  List<String> getSeriesY();

  @JsonProperty("seriesY")
  void setSeriesY(List<String> seriesY);

  @JsonProperty("seriesX")
  List<String> getSeriesX();

  @JsonProperty("seriesX")
  void setSeriesX(List<String> seriesX);

  @JsonProperty("smoothedMeanX")
  List<String> getSmoothedMeanX();

  @JsonProperty("smoothedMeanX")
  void setSmoothedMeanX(List<String> smoothedMeanX);

  @JsonProperty("smoothedMeanY")
  List<Number> getSmoothedMeanY();

  @JsonProperty("smoothedMeanY")
  void setSmoothedMeanY(List<Number> smoothedMeanY);

  @JsonProperty("smoothedMeanSE")
  List<Number> getSmoothedMeanSE();

  @JsonProperty("smoothedMeanSE")
  void setSmoothedMeanSE(List<Number> smoothedMeanSE);

  @JsonProperty("smoothedMeanError")
  String getSmoothedMeanError();

  @JsonProperty("smoothedMeanError")
  void setSmoothedMeanError(String smoothedMeanError);

  @JsonProperty("bestFitLineX")
  List<String> getBestFitLineX();

  @JsonProperty("bestFitLineX")
  void setBestFitLineX(List<String> bestFitLineX);

  @JsonProperty("bestFitLineY")
  List<Number> getBestFitLineY();

  @JsonProperty("bestFitLineY")
  void setBestFitLineY(List<Number> bestFitLineY);

  @JsonProperty("r2")
  Number getR2();

  @JsonProperty("r2")
  void setR2(Number r2);
}
