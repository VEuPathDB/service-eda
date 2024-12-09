package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "numeratorN",
    "denominatorN"
})
public class ProportionSampleSizeImpl implements ProportionSampleSize {
  @JsonProperty("numeratorN")
  private Number numeratorN;

  @JsonProperty("denominatorN")
  private Number denominatorN;

  @JsonProperty("numeratorN")
  public Number getNumeratorN() {
    return this.numeratorN;
  }

  @JsonProperty("numeratorN")
  public void setNumeratorN(Number numeratorN) {
    this.numeratorN = numeratorN;
  }

  @JsonProperty("denominatorN")
  public Number getDenominatorN() {
    return this.denominatorN;
  }

  @JsonProperty("denominatorN")
  public void setDenominatorN(Number denominatorN) {
    this.denominatorN = denominatorN;
  }
}
