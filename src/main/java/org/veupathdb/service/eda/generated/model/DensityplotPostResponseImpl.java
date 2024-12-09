package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "densityplot",
    "sampleSizeTable",
    "completeCasesTable"
})
public class DensityplotPostResponseImpl implements DensityplotPostResponse {
  @JsonProperty("densityplot")
  private Densityplot densityplot;

  @JsonProperty("sampleSizeTable")
  private List<SampleSizeTable> sampleSizeTable;

  @JsonProperty("completeCasesTable")
  private List<VariableCompleteCases> completeCasesTable;

  @JsonProperty("densityplot")
  public Densityplot getDensityplot() {
    return this.densityplot;
  }

  @JsonProperty("densityplot")
  public void setDensityplot(Densityplot densityplot) {
    this.densityplot = densityplot;
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
