package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ContTablePostResponseImpl.class
)
public interface ContTablePostResponse {
  @JsonProperty("mosaic")
  Mosaic getMosaic();

  @JsonProperty("mosaic")
  void setMosaic(Mosaic mosaic);

  @JsonProperty("sampleSizeTable")
  List<SampleSizeTable> getSampleSizeTable();

  @JsonProperty("sampleSizeTable")
  void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable);

  @JsonProperty("statsTable")
  List<ContTableStatsTable> getStatsTable();

  @JsonProperty("statsTable")
  void setStatsTable(List<ContTableStatsTable> statsTable);

  @JsonProperty("completeCasesTable")
  List<VariableCompleteCases> getCompleteCasesTable();

  @JsonProperty("completeCasesTable")
  void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable);
}
