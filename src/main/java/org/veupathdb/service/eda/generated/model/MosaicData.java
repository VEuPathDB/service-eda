package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MosaicDataImpl.class
)
public interface MosaicData {
  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("xLabel")
  List<String> getXLabel();

  @JsonProperty("xLabel")
  void setXLabel(List<String> xLabel);

  @JsonProperty("yLabel")
  List<List<String>> getYLabel();

  @JsonProperty("yLabel")
  void setYLabel(List<List<String>> yLabel);

  @JsonProperty("value")
  List<List<Number>> getValue();

  @JsonProperty("value")
  void setValue(List<List<Number>> value);
}
