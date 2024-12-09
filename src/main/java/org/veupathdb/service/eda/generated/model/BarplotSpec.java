package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BarplotSpecImpl.class
)
public interface BarplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("showMissingness")
  ShowMissingnessNoAxes getShowMissingness();

  @JsonProperty("showMissingness")
  void setShowMissingness(ShowMissingnessNoAxes showMissingness);

  @JsonProperty("barMode")
  BarModeType getBarMode();

  @JsonProperty("barMode")
  void setBarMode(BarModeType barMode);

  @JsonProperty("valueSpec")
  ValueSpec getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpec valueSpec);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("overlayVariable")
  VariableSpec getOverlayVariable();

  @JsonProperty("overlayVariable")
  void setOverlayVariable(VariableSpec overlayVariable);

  @JsonProperty("facetVariable")
  List<VariableSpec> getFacetVariable();

  @JsonProperty("facetVariable")
  void setFacetVariable(List<VariableSpec> facetVariable);

  enum BarModeType {
    @JsonProperty("group")
    GROUP("group"),

    @JsonProperty("stack")
    STACK("stack");

    public final String value;

    public String getValue() {
      return this.value;
    }

    BarModeType(String name) {
      this.value = name;
    }
  }
}
