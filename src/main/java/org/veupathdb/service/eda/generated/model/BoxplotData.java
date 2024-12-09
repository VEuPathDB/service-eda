package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BoxplotDataImpl.class
)
public interface BoxplotData {
  @JsonProperty("overlayVariableDetails")
  StrataVariableDetails getOverlayVariableDetails();

  @JsonProperty("overlayVariableDetails")
  void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails);

  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("lowerfence")
  List<Number> getLowerfence();

  @JsonProperty("lowerfence")
  void setLowerfence(List<Number> lowerfence);

  @JsonProperty("upperfence")
  List<Number> getUpperfence();

  @JsonProperty("upperfence")
  void setUpperfence(List<Number> upperfence);

  @JsonProperty("q1")
  List<Number> getQ1();

  @JsonProperty("q1")
  void setQ1(List<Number> q1);

  @JsonProperty("q3")
  List<Number> getQ3();

  @JsonProperty("q3")
  void setQ3(List<Number> q3);

  @JsonProperty("median")
  List<Number> getMedian();

  @JsonProperty("median")
  void setMedian(List<Number> median);

  @JsonProperty("outliers")
  List<List<Number>> getOutliers();

  @JsonProperty("outliers")
  void setOutliers(List<List<Number>> outliers);

  @JsonProperty("rawData")
  List<List<Number>> getRawData();

  @JsonProperty("rawData")
  void setRawData(List<List<Number>> rawData);

  @JsonProperty("mean")
  List<Number> getMean();

  @JsonProperty("mean")
  void setMean(List<Number> mean);

  @JsonProperty("label")
  List<String> getLabel();

  @JsonProperty("label")
  void setLabel(List<String> label);
}
