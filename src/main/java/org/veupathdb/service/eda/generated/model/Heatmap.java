package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = HeatmapImpl.class
)
public interface Heatmap {
  @JsonProperty("data")
  List<HeatmapData> getData();

  @JsonProperty("data")
  void setData(List<HeatmapData> data);

  @JsonProperty("config")
  PlotConfig getConfig();

  @JsonProperty("config")
  void setConfig(PlotConfig config);
}
