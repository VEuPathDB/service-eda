package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("boxplot")
public class FloatingBoxplotPostResponseImpl implements FloatingBoxplotPostResponse {
  @JsonProperty("boxplot")
  private FloatingBoxplot boxplot;

  @JsonProperty("boxplot")
  public FloatingBoxplot getBoxplot() {
    return this.boxplot;
  }

  @JsonProperty("boxplot")
  public void setBoxplot(FloatingBoxplot boxplot) {
    this.boxplot = boxplot;
  }
}
