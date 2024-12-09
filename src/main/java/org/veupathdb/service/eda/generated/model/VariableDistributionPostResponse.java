package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = VariableDistributionPostResponseImpl.class
)
public interface VariableDistributionPostResponse {
  @JsonProperty("histogram")
  List<HistogramBin> getHistogram();

  @JsonProperty("histogram")
  void setHistogram(List<HistogramBin> histogram);

  @JsonProperty("statistics")
  HistogramStats getStatistics();

  @JsonProperty("statistics")
  void setStatistics(HistogramStats statistics);
}
