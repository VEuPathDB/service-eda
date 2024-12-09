package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = HeatmapSpecImpl.class
)
public interface HeatmapSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("showMissingness")
  ShowMissingnessNoAxes getShowMissingness();

  @JsonProperty("showMissingness")
  void setShowMissingness(ShowMissingnessNoAxes showMissingness);

  @JsonProperty("valueSpec")
  ValueSpecType getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpecType valueSpec);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("yAxisVariable")
  VariableSpec getYAxisVariable();

  @JsonProperty("yAxisVariable")
  void setYAxisVariable(VariableSpec yAxisVariable);

  @JsonProperty("zAxisVariable")
  VariableSpec getZAxisVariable();

  @JsonProperty("zAxisVariable")
  void setZAxisVariable(VariableSpec zAxisVariable);

  @JsonProperty("facetVariable")
  List<VariableSpec> getFacetVariable();

  @JsonProperty("facetVariable")
  void setFacetVariable(List<VariableSpec> facetVariable);

  enum ValueSpecType {
    @JsonProperty("collection")
    COLLECTION("collection"),

    @JsonProperty("series")
    SERIES("series");

    public final String value;

    public String getValue() {
      return this.value;
    }

    ValueSpecType(String name) {
      this.value = name;
    }
  }
}
