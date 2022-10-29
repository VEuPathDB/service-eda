package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "variablePlotRef",
    "valuePlotRef"
})
public class PlotReferenceImpl implements PlotReference {
  @JsonProperty("id")
  private String id;

  @JsonProperty("variablePlotRef")
  private String variablePlotRef;

  @JsonProperty("valuePlotRef")
  private String valuePlotRef;

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("variablePlotRef")
  public String getVariablePlotRef() {
    return this.variablePlotRef;
  }

  @JsonProperty("variablePlotRef")
  public void setVariablePlotRef(String variablePlotRef) {
    this.variablePlotRef = variablePlotRef;
  }

  @JsonProperty("valuePlotRef")
  public String getValuePlotRef() {
    return this.valuePlotRef;
  }

  @JsonProperty("valuePlotRef")
  public void setValuePlotRef(String valuePlotRef) {
    this.valuePlotRef = valuePlotRef;
  }
}
