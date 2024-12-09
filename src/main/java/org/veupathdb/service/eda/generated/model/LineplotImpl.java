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
public class LineplotImpl implements Lineplot {
  @JsonProperty("data")
  private List<LineplotData> data;

  @JsonProperty("config")
  private LineplotConfig config;

  @JsonProperty("data")
  public List<LineplotData> getData() {
    return this.data;
  }

  @JsonProperty("data")
  public void setData(List<LineplotData> data) {
    this.data = data;
  }

  @JsonProperty("config")
  public LineplotConfig getConfig() {
    return this.config;
  }

  @JsonProperty("config")
  public void setConfig(LineplotConfig config) {
    this.config = config;
  }
}
