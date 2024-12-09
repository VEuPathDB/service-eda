package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = HistogramDataImpl.class
)
public interface HistogramData {
  @JsonProperty("overlayVariableDetails")
  StrataVariableDetails getOverlayVariableDetails();

  @JsonProperty("overlayVariableDetails")
  void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails);

  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("value")
  List<Number> getValue();

  @JsonProperty("value")
  void setValue(List<Number> value);

  @JsonProperty("binStart")
  List<String> getBinStart();

  @JsonProperty("binStart")
  void setBinStart(List<String> binStart);

  @JsonProperty("binEnd")
  List<String> getBinEnd();

  @JsonProperty("binEnd")
  void setBinEnd(List<String> binEnd);

  @JsonProperty("binLabel")
  List<String> getBinLabel();

  @JsonProperty("binLabel")
  void setBinLabel(List<String> binLabel);
}
