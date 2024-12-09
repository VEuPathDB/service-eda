package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "overlayType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.ContinousOverlayConfig.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.CategoricalOverlayConfig.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.OverlayConfig.class)
})
@JsonDeserialize(
    as = OverlayConfigImpl.class
)
public interface OverlayConfig {
  OverlayType _DISCRIMINATOR_TYPE_NAME = null;

  @JsonProperty("overlayType")
  OverlayType getOverlayType();

  @JsonProperty("overlayVariable")
  VariableSpec getOverlayVariable();

  @JsonProperty("overlayVariable")
  void setOverlayVariable(VariableSpec overlayVariable);
}
