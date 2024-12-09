package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("scatterplot")
public class FloatingScatterplotPostResponseImpl implements FloatingScatterplotPostResponse {
  @JsonProperty("scatterplot")
  private FloatingScatterplot scatterplot;

  @JsonProperty("scatterplot")
  public FloatingScatterplot getScatterplot() {
    return this.scatterplot;
  }

  @JsonProperty("scatterplot")
  public void setScatterplot(FloatingScatterplot scatterplot) {
    this.scatterplot = scatterplot;
  }
}
