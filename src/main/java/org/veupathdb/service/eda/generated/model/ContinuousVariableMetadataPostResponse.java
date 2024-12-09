package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ContinuousVariableMetadataPostResponseImpl.class
)
public interface ContinuousVariableMetadataPostResponse {
  @JsonProperty("binRanges")
  AllBinRanges getBinRanges();

  @JsonProperty("binRanges")
  void setBinRanges(AllBinRanges binRanges);

  @JsonProperty("median")
  Number getMedian();

  @JsonProperty("median")
  void setMedian(Number median);
}
