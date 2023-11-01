package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "statistics",
    "data1Metadata",
    "data2Metadata"
})
public class CorrelationStatsResponseImpl implements CorrelationStatsResponse {
  @JsonProperty("statistics")
  private List<CorrelationPoint> statistics;

  @JsonProperty("data1Metadata")
  private String data1Metadata;

  @JsonProperty("data2Metadata")
  private String data2Metadata;

  @JsonProperty("statistics")
  public List<CorrelationPoint> getStatistics() {
    return this.statistics;
  }

  @JsonProperty("statistics")
  public void setStatistics(List<CorrelationPoint> statistics) {
    this.statistics = statistics;
  }

  @JsonProperty("data1Metadata")
  public String getData1Metadata() {
    return this.data1Metadata;
  }

  @JsonProperty("data1Metadata")
  public void setData1Metadata(String data1Metadata) {
    this.data1Metadata = data1Metadata;
  }

  @JsonProperty("data2Metadata")
  public String getData2Metadata() {
    return this.data2Metadata;
  }

  @JsonProperty("data2Metadata")
  public void setData2Metadata(String data2Metadata) {
    this.data2Metadata = data2Metadata;
  }
}
