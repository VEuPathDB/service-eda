package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = CollectionFloatingLineplotSpecImpl.class
)
public interface CollectionFloatingLineplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("xAxisVariable")
  VariableSpec getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(VariableSpec xAxisVariable);

  @JsonProperty("yAxisNumeratorValues")
  List<String> getYAxisNumeratorValues();

  @JsonProperty("yAxisNumeratorValues")
  void setYAxisNumeratorValues(List<String> yAxisNumeratorValues);

  @JsonProperty("yAxisDenominatorValues")
  List<String> getYAxisDenominatorValues();

  @JsonProperty("yAxisDenominatorValues")
  void setYAxisDenominatorValues(List<String> yAxisDenominatorValues);

  @JsonProperty("overlayConfig")
  CollectionOverlayConfigWithValues getOverlayConfig();

  @JsonProperty("overlayConfig")
  void setOverlayConfig(CollectionOverlayConfigWithValues overlayConfig);

  @JsonProperty("binSpec")
  BinWidthSpec getBinSpec();

  @JsonProperty("binSpec")
  void setBinSpec(BinWidthSpec binSpec);

  @JsonProperty("valueSpec")
  ValueSpecType getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpecType valueSpec);

  @JsonProperty("errorBars")
  StringBoolean getErrorBars();

  @JsonProperty("errorBars")
  void setErrorBars(StringBoolean errorBars);

  @JsonProperty("viewport")
  NumericViewport getViewport();

  @JsonProperty("viewport")
  void setViewport(NumericViewport viewport);

  enum ValueSpecType {
    @JsonProperty("median")
    MEDIAN("median"),

    @JsonProperty("mean")
    MEAN("mean"),

    @JsonProperty("geometricMean")
    GEOMETRICMEAN("geometricMean"),

    @JsonProperty("proportion")
    PROPORTION("proportion");

    public final String value;

    public String getValue() {
      return this.value;
    }

    ValueSpecType(String name) {
      this.value = name;
    }
  }
}
