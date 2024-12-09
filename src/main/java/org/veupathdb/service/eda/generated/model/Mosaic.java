package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = MosaicImpl.class
)
public interface Mosaic {
  @JsonProperty("data")
  List<MosaicData> getData();

  @JsonProperty("data")
  void setData(List<MosaicData> data);

  @JsonProperty("config")
  PlotConfig getConfig();

  @JsonProperty("config")
  void setConfig(PlotConfig config);
}
