package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "left",
    "right"
})
public class LongitudeViewportImpl implements LongitudeViewport {
  @JsonProperty("left")
  private Number left;

  @JsonProperty("right")
  private Number right;

  @JsonProperty("left")
  public Number getLeft() {
    return this.left;
  }

  @JsonProperty("left")
  public void setLeft(Number left) {
    this.left = left;
  }

  @JsonProperty("right")
  public Number getRight() {
    return this.right;
  }

  @JsonProperty("right")
  public void setRight(Number right) {
    this.right = right;
  }
}
