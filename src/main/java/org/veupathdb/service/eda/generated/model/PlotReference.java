package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = PlotReferenceImpl.class
)
public interface PlotReference {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("variablePlotRef")
  String getVariablePlotRef();

  @JsonProperty("variablePlotRef")
  void setVariablePlotRef(String variablePlotRef);

  @JsonProperty("valuePlotRef")
  String getValuePlotRef();

  @JsonProperty("valuePlotRef")
  void setValuePlotRef(String valuePlotRef);
}
