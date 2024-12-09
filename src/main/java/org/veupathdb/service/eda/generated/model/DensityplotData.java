package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DensityplotDataImpl.class
)
public interface DensityplotData {
  @JsonProperty("overlayVariableDetails")
  StrataVariableDetails getOverlayVariableDetails();

  @JsonProperty("overlayVariableDetails")
  void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails);

  @JsonProperty("facetVariableDetails")
  List<StrataVariableDetails> getFacetVariableDetails();

  @JsonProperty("facetVariableDetails")
  void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails);

  @JsonProperty("densityY")
  List<Number> getDensityY();

  @JsonProperty("densityY")
  void setDensityY(List<Number> densityY);

  @JsonProperty("densityX")
  List<Number> getDensityX();

  @JsonProperty("densityX")
  void setDensityX(List<Number> densityX);
}
