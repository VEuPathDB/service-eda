package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = LineplotImpl.class
)
public interface Lineplot {
  @JsonProperty("data")
  List<LineplotData> getData();

  @JsonProperty("data")
  void setData(List<LineplotData> data);

  @JsonProperty("config")
  LineplotConfig getConfig();

  @JsonProperty("config")
  void setConfig(LineplotConfig config);
}
