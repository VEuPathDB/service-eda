package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overlayType",
    "overlayVariable"
})
public class OverlayConfigImpl implements OverlayConfig {
  @JsonProperty("overlayType")
  private final OverlayType overlayType = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("overlayVariable")
  private VariableSpec overlayVariable;

  @JsonProperty("overlayType")
  public OverlayType getOverlayType() {
    return this.overlayType;
  }

  @JsonProperty("overlayVariable")
  public VariableSpec getOverlayVariable() {
    return this.overlayVariable;
  }

  @JsonProperty("overlayVariable")
  public void setOverlayVariable(VariableSpec overlayVariable) {
    this.overlayVariable = overlayVariable;
  }
}
