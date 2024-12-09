package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = FloatingContTablePostResponseImpl.class
)
public interface FloatingContTablePostResponse {
  @JsonProperty("mosaic")
  FloatingContTable getMosaic();

  @JsonProperty("mosaic")
  void setMosaic(FloatingContTable mosaic);

  @JsonProperty("statsTable")
  List<TwoByTwoStatsTable> getStatsTable();

  @JsonProperty("statsTable")
  void setStatsTable(List<TwoByTwoStatsTable> statsTable);
}
