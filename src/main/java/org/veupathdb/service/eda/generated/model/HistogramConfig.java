package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = HistogramConfigImpl.class
)
public interface HistogramConfig extends PlotConfig {
  @JsonProperty("completeCasesAllVars")
  Number getCompleteCasesAllVars();

  @JsonProperty("completeCasesAllVars")
  void setCompleteCasesAllVars(Number completeCasesAllVars);

  @JsonProperty("completeCasesAxesVars")
  Number getCompleteCasesAxesVars();

  @JsonProperty("completeCasesAxesVars")
  void setCompleteCasesAxesVars(Number completeCasesAxesVars);

  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);

  @JsonProperty("binSlider")
  BinSlider getBinSlider();

  @JsonProperty("binSlider")
  void setBinSlider(BinSlider binSlider);

  @JsonProperty("binSpec")
  BinSpec getBinSpec();

  @JsonProperty("binSpec")
  void setBinSpec(BinSpec binSpec);

  @JsonProperty("summary")
  HistogramSummary getSummary();

  @JsonProperty("summary")
  void setSummary(HistogramSummary summary);

  @JsonProperty("viewport")
  NumericViewport getViewport();

  @JsonProperty("viewport")
  void setViewport(NumericViewport viewport);
}
