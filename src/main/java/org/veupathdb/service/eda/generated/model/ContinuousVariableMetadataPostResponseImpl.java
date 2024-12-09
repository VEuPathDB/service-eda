package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "binRanges",
    "median"
})
public class ContinuousVariableMetadataPostResponseImpl implements ContinuousVariableMetadataPostResponse {
  @JsonProperty("binRanges")
  private AllBinRanges binRanges;

  @JsonProperty("median")
  private Number median;

  @JsonProperty("binRanges")
  public AllBinRanges getBinRanges() {
    return this.binRanges;
  }

  @JsonProperty("binRanges")
  public void setBinRanges(AllBinRanges binRanges) {
    this.binRanges = binRanges;
  }

  @JsonProperty("median")
  public Number getMedian() {
    return this.median;
  }

  @JsonProperty("median")
  public void setMedian(Number median) {
    this.median = median;
  }
}
