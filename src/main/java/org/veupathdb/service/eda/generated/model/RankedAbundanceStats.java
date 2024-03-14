package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = RankedAbundanceStatsImpl.class
)
public interface RankedAbundanceStats {
  @JsonProperty("isCutoff")
  Boolean getIsCutoff();

  @JsonProperty("isCutoff")
  void setIsCutoff(Boolean isCutoff);
}
