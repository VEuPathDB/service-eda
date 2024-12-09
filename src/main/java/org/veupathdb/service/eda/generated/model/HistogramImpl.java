package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "config"
})
public class HistogramImpl implements Histogram {
  @JsonProperty("data")
  private List<HistogramData> data;

  @JsonProperty("config")
  private HistogramConfig config;

  @JsonProperty("data")
  public List<HistogramData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<HistogramData> data) {
    this.data = data;
  }

  @JsonProperty("config")
  public HistogramConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(HistogramConfig config) {
    this.config = config;
  }
}
