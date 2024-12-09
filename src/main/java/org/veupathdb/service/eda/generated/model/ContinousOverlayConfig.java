package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonTypeName("continuous")
@JsonDeserialize(
    as = ContinousOverlayConfigImpl.class
)
public interface ContinousOverlayConfig extends OverlayConfig {
  OverlayType _DISCRIMINATOR_TYPE_NAME = OverlayType.CONTINUOUS;

  @JsonProperty("overlayType")
  OverlayType getOverlayType();

  @JsonProperty("overlayVariable")
  VariableSpec getOverlayVariable();

  @JsonProperty("overlayVariable")
  void setOverlayVariable(VariableSpec overlayVariable);

  @JsonProperty("overlayValues")
  List<LegacyLabeledRange> getOverlayValues();

  @JsonProperty("overlayValues")
  void setOverlayValues(List<LegacyLabeledRange> overlayValues);
}
