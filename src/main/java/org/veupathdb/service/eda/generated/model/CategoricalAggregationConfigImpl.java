package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("categorical")
@JsonPropertyOrder({
    "overlayType",
    "numeratorValues",
    "denominatorValues"
})
public class CategoricalAggregationConfigImpl implements CategoricalAggregationConfig {
  @JsonProperty("overlayType")
  private final OverlayType overlayType = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("numeratorValues")
  private List<String> numeratorValues;

  @JsonProperty("denominatorValues")
  private List<String> denominatorValues;

  @JsonProperty("overlayType")
  public OverlayType getOverlayType() {
    return this.overlayType;
  }

  @JsonProperty("numeratorValues")
  public List<String> getNumeratorValues() {
    return this.numeratorValues;
  }

  @JsonProperty("numeratorValues")
  public void setNumeratorValues(List<String> numeratorValues) {
    this.numeratorValues = numeratorValues;
  }

  @JsonProperty("denominatorValues")
  public List<String> getDenominatorValues() {
    return this.denominatorValues;
  }

  @JsonProperty("denominatorValues")
  public void setDenominatorValues(List<String> denominatorValues) {
    this.denominatorValues = denominatorValues;
  }
}
