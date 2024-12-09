package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = NumericViewportImpl.class
)
public interface NumericViewport {
  @JsonProperty("xMin")
  String getXMin();

  @JsonProperty("xMin")
  void setXMin(String xMin);

  @JsonProperty("xMax")
  String getXMax();

  @JsonProperty("xMax")
  void setXMax(String xMax);
}
