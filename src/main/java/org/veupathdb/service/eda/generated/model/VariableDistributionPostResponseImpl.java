package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "histogram",
    "statistics"
})
public class VariableDistributionPostResponseImpl implements VariableDistributionPostResponse {
  @JsonProperty("histogram")
  private List<HistogramBin> histogram;

  @JsonProperty("statistics")
  private HistogramStats statistics;

  @JsonProperty("histogram")
  public List<HistogramBin> getHistogram() {
    return this.histogram;
  }

  @JsonProperty("histogram")
  public void setHistogram(List<HistogramBin> histogram) {
    this.histogram = histogram;
  }

  @JsonProperty("statistics")
  public HistogramStats getStatistics() {
    return this.statistics;
  }

  @JsonProperty("statistics")
  public void setStatistics(HistogramStats statistics) {
    this.statistics = statistics;
  }
}
