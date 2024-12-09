package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "histogram",
    "sampleSizeTable",
    "completeCasesTable"
})
public class HistogramPostResponseImpl implements HistogramPostResponse {
  @JsonProperty("histogram")
  private Histogram histogram;

  @JsonProperty("sampleSizeTable")
  private List<SampleSizeTable> sampleSizeTable;

  @JsonProperty("completeCasesTable")
  private List<VariableCompleteCases> completeCasesTable;

  @JsonProperty("histogram")
  public Histogram getHistogram() {
    return this.histogram;
  }

  @JsonProperty("histogram")
  public void setHistogram(Histogram histogram) {
    this.histogram = histogram;
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
}
