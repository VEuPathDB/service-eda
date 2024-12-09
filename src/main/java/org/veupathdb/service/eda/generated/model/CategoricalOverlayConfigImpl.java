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
    "overlayVariable",
    "overlayValues"
})
public class CategoricalOverlayConfigImpl implements CategoricalOverlayConfig {
  @JsonProperty("overlayType")
  private final OverlayType overlayType = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("overlayVariable")
  private VariableSpec overlayVariable;

  @JsonProperty("overlayValues")
  private List<String> overlayValues;

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

  @JsonProperty("overlayValues")
  public List<String> getOverlayValues() {
    return this.overlayValues;
  }

  @JsonProperty("overlayValues")
  public void setOverlayValues(List<String> overlayValues) {
    this.overlayValues = overlayValues;
  }
}
