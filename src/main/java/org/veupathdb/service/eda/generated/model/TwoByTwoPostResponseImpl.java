package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mosaic",
    "sampleSizeTable",
    "statsTable",
    "completeCasesTable"
})
public class TwoByTwoPostResponseImpl implements TwoByTwoPostResponse {
  @JsonProperty("mosaic")
  private Mosaic mosaic;

  @JsonProperty("sampleSizeTable")
  private List<SampleSizeTable> sampleSizeTable;

  @JsonProperty("statsTable")
  private List<TwoByTwoStatsTable> statsTable;

  @JsonProperty("completeCasesTable")
  private List<VariableCompleteCases> completeCasesTable;

  @JsonProperty("mosaic")
  public Mosaic getMosaic() {
    return this.mosaic;
  }

  @JsonProperty("mosaic")
  public void setMosaic(Mosaic mosaic) {
    this.mosaic = mosaic;
  }

  @JsonProperty("sampleSizeTable")
  public List<SampleSizeTable> getSampleSizeTable() {
    return this.sampleSizeTable;
  }

  @JsonProperty("sampleSizeTable")
  public void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable) {
    this.sampleSizeTable = sampleSizeTable;
  }

  @JsonProperty("statsTable")
  public List<TwoByTwoStatsTable> getStatsTable() {
    return this.statsTable;
  }

  @JsonProperty("statsTable")
  public void setStatsTable(List<TwoByTwoStatsTable> statsTable) {
    this.statsTable = statsTable;
  }

  @JsonProperty("completeCasesTable")
  public List<VariableCompleteCases> getCompleteCasesTable() {
    return this.completeCasesTable;
  }

  @JsonProperty("completeCasesTable")
  public void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable) {
    this.completeCasesTable = completeCasesTable;
  }
}
