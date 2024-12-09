package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BarplotPostResponseImpl.class
)
public interface BarplotPostResponse {
  @JsonProperty("barplot")
  Barplot getBarplot();

  @JsonProperty("barplot")
  void setBarplot(Barplot barplot);

  @JsonProperty("sampleSizeTable")
  List<SampleSizeTable> getSampleSizeTable();

  @JsonProperty("sampleSizeTable")
  void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable);

  @JsonProperty("completeCasesTable")
  List<VariableCompleteCases> getCompleteCasesTable();

  @JsonProperty("completeCasesTable")
  void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable);
}
