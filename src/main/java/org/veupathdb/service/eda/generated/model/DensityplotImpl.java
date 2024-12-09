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
public class DensityplotImpl implements Densityplot {
  @JsonProperty("data")
  private List<DensityplotData> data;

  @JsonProperty("config")
  private PlotConfig config;

  @JsonProperty("data")
  public List<DensityplotData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<DensityplotData> data) {
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
