package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = LineplotDataImpl.class
)
public interface LineplotData {
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

  @JsonProperty("binStart")
  List<String> getBinStart();

  @JsonProperty("binStart")
  void setBinStart(List<String> binStart);

  @JsonProperty("binEnd")
  List<String> getBinEnd();

  @JsonProperty("binEnd")
  void setBinEnd(List<String> binEnd);

  @JsonProperty("binSampleSize")
  List<SampleSize> getBinSampleSize();

  @JsonProperty("binSampleSize")
  void setBinSampleSize(List<SampleSize> binSampleSize);

  @JsonProperty("errorBars")
  List<ErrorBar> getErrorBars();

  @JsonProperty("errorBars")
  void setErrorBars(List<ErrorBar> errorBars);
}
