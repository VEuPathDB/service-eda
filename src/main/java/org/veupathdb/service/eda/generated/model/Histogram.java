package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = HistogramImpl.class
)
public interface Histogram {
  @JsonProperty("data")
  List<HistogramData> getData();

  @JsonProperty("data")
  void setData(List<HistogramData> data);

  @JsonProperty("config")
  HistogramConfig getConfig();

  @JsonProperty("config")
  void setConfig(HistogramConfig config);
}
