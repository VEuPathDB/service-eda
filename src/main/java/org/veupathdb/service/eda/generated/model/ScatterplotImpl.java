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
public class ScatterplotImpl implements Scatterplot {
  @JsonProperty("data")
  private List<ScatterplotData> data;

  @JsonProperty("config")
  private PlotConfig config;

  @JsonProperty("data")
  public List<ScatterplotData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<ScatterplotData> data) {
    this.data = data;
  }

  @JsonProperty("config")
  public PlotConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(PlotConfig config) {
    this.config = config;
  }
}
