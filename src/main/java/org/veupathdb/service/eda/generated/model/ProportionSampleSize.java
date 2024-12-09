package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ProportionSampleSizeImpl.class
)
public interface ProportionSampleSize {
  @JsonProperty("numeratorN")
  Number getNumeratorN();

  @JsonProperty("numeratorN")
  void setNumeratorN(Number numeratorN);

  @JsonProperty("denominatorN")
  Number getDenominatorN();

  @JsonProperty("denominatorN")
  void setDenominatorN(Number denominatorN);
}
