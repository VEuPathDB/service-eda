package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("isCutoff")
public class RankedAbundanceStatsImpl implements RankedAbundanceStats {
  @JsonProperty("isCutoff")
  private Boolean isCutoff;

  @JsonProperty("isCutoff")
  public Boolean getIsCutoff() {
    return this.isCutoff;
  }

  @JsonProperty("isCutoff")
  public void setIsCutoff(Boolean isCutoff) {
    this.isCutoff = isCutoff;
  }
}
