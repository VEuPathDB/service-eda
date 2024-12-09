package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = BarplotImpl.class
)
public interface Barplot {
  @JsonProperty("data")
  List<BarplotData> getData();

  @JsonProperty("data")
  void setData(List<BarplotData> data);

  @JsonProperty("config")
  PlotConfig getConfig();

  @JsonProperty("config")
  void setConfig(PlotConfig config);
}
