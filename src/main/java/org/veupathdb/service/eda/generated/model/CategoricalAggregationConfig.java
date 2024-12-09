package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonTypeName("categorical")
@JsonDeserialize(
    as = CategoricalAggregationConfigImpl.class
)
public interface CategoricalAggregationConfig extends QuantitativeAggregationConfig {
  OverlayType _DISCRIMINATOR_TYPE_NAME = OverlayType.CATEGORICAL;

  @JsonProperty("overlayType")
  OverlayType getOverlayType();

  @JsonProperty("numeratorValues")
  List<String> getNumeratorValues();

  @JsonProperty("numeratorValues")
  void setNumeratorValues(List<String> numeratorValues);

  @JsonProperty("denominatorValues")
  List<String> getDenominatorValues();

  @JsonProperty("denominatorValues")
  void setDenominatorValues(List<String> denominatorValues);
}
