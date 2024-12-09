package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("N")
public class SimpleSampleSizeImpl implements SimpleSampleSize {
  @JsonProperty("N")
  private Number n;

  @JsonProperty("N")
  public Number getN() {
    return this.n;
  }

  @JsonProperty("N")
  public void setN(Number n) {
    this.n = n;
  }
}
