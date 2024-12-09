package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ScatterplotImpl.class
)
public interface Scatterplot {
  @JsonProperty("data")
  List<ScatterplotData> getData();

  @JsonProperty("data")
  void setData(List<ScatterplotData> data);

  @JsonProperty("config")
  PlotConfig getConfig();

  @JsonProperty("config")
  void setConfig(PlotConfig config);
}
