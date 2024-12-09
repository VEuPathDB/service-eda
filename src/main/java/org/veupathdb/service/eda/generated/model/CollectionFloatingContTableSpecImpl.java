package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "outputEntityId",
    "xAxisVariable"
})
public class CollectionFloatingContTableSpecImpl implements CollectionFloatingContTableSpec {
  @JsonProperty("outputEntityId")
  private String outputEntityId;

  @JsonProperty("xAxisVariable")
  private CollectionOverlayConfigWithValues xAxisVariable;

  @JsonProperty("outputEntityId")
  public String getOutputEntityId() {
    return this.outputEntityId;
  }

  @JsonProperty("outputEntityId")
  public void setOutputEntityId(String outputEntityId) {
    this.outputEntityId = outputEntityId;
  }

  @JsonProperty("xAxisVariable")
  public CollectionOverlayConfigWithValues getXAxisVariable() {
    return this.xAxisVariable;
  }

  @JsonProperty("xAxisVariable")
  public void setXAxisVariable(CollectionOverlayConfigWithValues xAxisVariable) {
    this.xAxisVariable = xAxisVariable;
  }
}
