package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonTypeName("categorical")
@JsonDeserialize(
    as = CategoricalOverlayConfigImpl.class
)
public interface CategoricalOverlayConfig extends OverlayConfig {
  OverlayType _DISCRIMINATOR_TYPE_NAME = OverlayType.CATEGORICAL;

  @JsonProperty("overlayType")
  OverlayType getOverlayType();

  @JsonProperty("overlayVariable")
  VariableSpec getOverlayVariable();

  @JsonProperty("overlayVariable")
  void setOverlayVariable(VariableSpec overlayVariable);

  @JsonProperty("overlayValues")
  List<String> getOverlayValues();

  @JsonProperty("overlayValues")
  void setOverlayValues(List<String> overlayValues);
}
