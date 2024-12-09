package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("lineplot")
public class FloatingLineplotPostResponseImpl implements FloatingLineplotPostResponse {
  @JsonProperty("lineplot")
  private FloatingLineplot lineplot;

  @JsonProperty("lineplot")
  public FloatingLineplot getLineplot() {
    return this.lineplot;
  }

  @JsonProperty("lineplot")
  public void setLineplot(FloatingLineplot lineplot) {
    this.lineplot = lineplot;
  }
}
