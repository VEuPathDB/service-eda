package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = AllBinRangesImpl.class
)
public interface AllBinRanges {
  @JsonProperty("equalInterval")
  List<LabeledRange> getEqualInterval();

  @JsonProperty("equalInterval")
  void setEqualInterval(List<LabeledRange> equalInterval);

  @JsonProperty("quantile")
  List<LabeledRange> getQuantile();

  @JsonProperty("quantile")
  void setQuantile(List<LabeledRange> quantile);

  @JsonProperty("standardDeviation")
  List<LabeledRange> getStandardDeviation();

  @JsonProperty("standardDeviation")
  void setStandardDeviation(List<LabeledRange> standardDeviation);
}
