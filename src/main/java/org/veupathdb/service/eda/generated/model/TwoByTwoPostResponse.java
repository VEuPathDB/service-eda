package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = TwoByTwoPostResponseImpl.class
)
public interface TwoByTwoPostResponse {
  @JsonProperty("mosaic")
  Mosaic getMosaic();

  @JsonProperty("mosaic")
  void setMosaic(Mosaic mosaic);

  @JsonProperty("sampleSizeTable")
  List<SampleSizeTable> getSampleSizeTable();

  @JsonProperty("sampleSizeTable")
  void setSampleSizeTable(List<SampleSizeTable> sampleSizeTable);

  @JsonProperty("statsTable")
  List<TwoByTwoStatsTable> getStatsTable();

  @JsonProperty("statsTable")
  void setStatsTable(List<TwoByTwoStatsTable> statsTable);

  @JsonProperty("completeCasesTable")
  List<VariableCompleteCases> getCompleteCasesTable();

  @JsonProperty("completeCasesTable")
  void setCompleteCasesTable(List<VariableCompleteCases> completeCasesTable);
}
