package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "boxplot",
    "sampleSizeTable",
    "completeCasesTable",
    "statsTable"
})
public class BoxplotPostResponseImpl implements BoxplotPostResponse {
  @JsonProperty("boxplot")
  private Boxplot boxplot;

  @JsonProperty("sampleSizeTable")
  private List<SampleSizeTable> sampleSizeTable;

  @JsonProperty("completeCasesTable")
  private List<VariableCompleteCases> completeCasesTable;

  @JsonProperty("statsTable")
  private List<BoxplotStatsTable> statsTable;

  @JsonProperty("boxplot")
  public Boxplot getBoxplot() {
    return this.boxplot;
  }

  @JsonProperty("boxplot")
  public void setBoxplot(Boxplot boxplot) {
    this.boxplot = boxplot;
  }

  @JsonProperty("sampleSizeTable")
  public List<SampleSizeTable> getSampleSizeTable() {
    return this.sampleSizeTable;
  }

  @JsonProperty("sampleSizeTable")
  public void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable) {
    this.sampleSizeTable = sampleSizeTable;
  }

  @JsonProperty("completeCasesTable")
  public List<VariableCompleteCases> getCompleteCasesTable() {
    return this.completeCasesTable;
  }

  @JsonProperty("completeCasesTable")
  public void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable) {
    this.completeCasesTable = completeCasesTable;
  }

  @JsonProperty("statsTable")
  public List<BoxplotStatsTable> getStatsTable() {
    return this.statsTable;
  }

  @JsonProperty("statsTable")
  public void setStatsTable(List<BoxplotStatsTable> statsTable) {
    this.statsTable = statsTable;
  }
}
