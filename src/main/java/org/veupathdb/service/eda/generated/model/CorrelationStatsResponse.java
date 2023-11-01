package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = CorrelationStatsResponseImpl.class
)
public interface CorrelationStatsResponse {
  @JsonProperty("statistics")
  List<CorrelationPoint> getStatistics();

  @JsonProperty("statistics")
  void setStatistics(List<CorrelationPoint> statistics);

  @JsonProperty("data1Metadata")
  String getData1Metadata();

  @JsonProperty("data1Metadata")
  void setData1Metadata(String data1Metadata);

  @JsonProperty("data2Metadata")
  String getData2Metadata();

  @JsonProperty("data2Metadata")
  void setData2Metadata(String data2Metadata);
}
