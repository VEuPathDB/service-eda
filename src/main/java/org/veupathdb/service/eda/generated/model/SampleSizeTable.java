package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = SampleSizeTableImpl.class
)
public interface SampleSizeTable {
  @JsonProperty("xVariableDetails")
  List<StrataVariableDetails> getXVariableDetails();

  @JsonProperty("xVariableDetails")
  void setXVariableDetails(List<StrataVariableDetails> xVariableDetails);

  @JsonProperty("overlayVariableDetails")
  StrataVariableDetails getOverlayVariableDetails();

  @JsonProperty("overlayVariableDetails")
  void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails);

  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("size")
  List<Number> getSize();

  @JsonProperty("size")
  void setSize(List<Number> size);
}
