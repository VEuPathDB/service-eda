package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = APIIntegerDistributionDefaultsImpl.class
)
public interface APIIntegerDistributionDefaults {
  @JsonProperty("displayRangeMin")
  Long getDisplayRangeMin();

  @JsonProperty("displayRangeMin")
  void setDisplayRangeMin(Long displayRangeMin);

  @JsonProperty("displayRangeMax")
  Long getDisplayRangeMax();

  @JsonProperty("displayRangeMax")
  void setDisplayRangeMax(Long displayRangeMax);

  @JsonProperty("rangeMin")
  Long getRangeMin();

  @JsonProperty("rangeMin")
  void setRangeMin(Long rangeMin);

  @JsonProperty("rangeMax")
  Long getRangeMax();

  @JsonProperty("rangeMax")
  void setRangeMax(Long rangeMax);

  @JsonProperty("binWidth")
  Long getBinWidth();

  @JsonProperty("binWidth")
  void setBinWidth(Long binWidth);

  @JsonProperty("binWidthOverride")
  Long getBinWidthOverride();

  @JsonProperty("binWidthOverride")
  void setBinWidthOverride(Long binWidthOverride);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
