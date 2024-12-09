package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BoxplotPostResponseImpl.class
)
public interface BoxplotPostResponse {
  @JsonProperty("boxplot")
  Boxplot getBoxplot();

  @JsonProperty("boxplot")
  void setBoxplot(Boxplot boxplot);

  @JsonProperty("sampleSizeTable")
  List<SampleSizeTable> getSampleSizeTable();

  @JsonProperty("sampleSizeTable")
  void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable);

  @JsonProperty("completeCasesTable")
  List<VariableCompleteCases> getCompleteCasesTable();

  @JsonProperty("completeCasesTable")
  void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable);

  @JsonProperty("statsTable")
  List<BoxplotStatsTable> getStatsTable();

  @JsonProperty("statsTable")
  void setStatsTable(List<BoxplotStatsTable> statsTable);
}
