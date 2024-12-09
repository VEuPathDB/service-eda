package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = DensityplotImpl.class
)
public interface Densityplot {
  @JsonProperty("data")
  List<DensityplotData> getData();

  @JsonProperty("data")
  void setData(List<DensityplotData> data);

  @JsonProperty("config")
  PlotConfig getConfig();

  @JsonProperty("config")
  void setConfig(PlotConfig config);
}
