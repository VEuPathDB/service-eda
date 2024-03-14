package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = APINumberDistributionDefaultsImpl.class
)
public interface APINumberDistributionDefaults {
  @JsonProperty("displayRangeMin")
  Number getDisplayRangeMin();

  @JsonProperty("displayRangeMin")
  void setDisplayRangeMin(Number displayRangeMin);

  @JsonProperty("displayRangeMax")
  Number getDisplayRangeMax();

  @JsonProperty("displayRangeMax")
  void setDisplayRangeMax(Number displayRangeMax);

  @JsonProperty("rangeMin")
  Number getRangeMin();

  @JsonProperty("rangeMin")
  void setRangeMin(Number rangeMin);

  @JsonProperty("rangeMax")
  Number getRangeMax();

  @JsonProperty("rangeMax")
  void setRangeMax(Number rangeMax);

  @JsonProperty("binWidth")
  Number getBinWidth();

  @JsonProperty("binWidth")
  void setBinWidth(Number binWidth);

  @JsonProperty("binWidthOverride")
  Number getBinWidthOverride();

  @JsonProperty("binWidthOverride")
  void setBinWidthOverride(Number binWidthOverride);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
