package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mosaic",
    "statsTable"
})
public class FloatingContTablePostResponseImpl implements FloatingContTablePostResponse {
  @JsonProperty("mosaic")
  private FloatingContTable mosaic;

  @JsonProperty("statsTable")
  private List<TwoByTwoStatsTable> statsTable;

  @JsonProperty("mosaic")
  public FloatingContTable getMosaic() {
    return this.mosaic;
  }

  @JsonProperty("mosaic")
  public void setMosaic(FloatingContTable mosaic) {
    this.mosaic = mosaic;
  }

  @JsonProperty("statsTable")
  public List<TwoByTwoStatsTable> getStatsTable() {
    return this.statsTable;
  }

  @JsonProperty("statsTable")
  public void setStatsTable(List<TwoByTwoStatsTable> statsTable) {
    this.statsTable = statsTable;
  }
}
