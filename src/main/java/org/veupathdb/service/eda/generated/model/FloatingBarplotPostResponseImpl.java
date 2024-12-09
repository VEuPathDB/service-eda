package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("barplot")
public class FloatingBarplotPostResponseImpl implements FloatingBarplotPostResponse {
  @JsonProperty("barplot")
  private FloatingBarplot barplot;

  @JsonProperty("barplot")
  public FloatingBarplot getBarplot() {
    return this.barplot;
  }

  @JsonProperty("barplot")
  public void setBarplot(FloatingBarplot barplot) {
    this.barplot = barplot;
  }
}
