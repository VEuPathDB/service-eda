package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = APIDateDistributionDefaultsImpl.class
)
public interface APIDateDistributionDefaults {
  @JsonProperty("displayRangeMin")
  String getDisplayRangeMin();

  @JsonProperty("displayRangeMin")
  void setDisplayRangeMin(String displayRangeMin);

  @JsonProperty("displayRangeMax")
  String getDisplayRangeMax();

  @JsonProperty("displayRangeMax")
  void setDisplayRangeMax(String displayRangeMax);

  @JsonProperty("rangeMin")
  String getRangeMin();

  @JsonProperty("rangeMin")
  void setRangeMin(String rangeMin);

  @JsonProperty("rangeMax")
  String getRangeMax();

  @JsonProperty("rangeMax")
  void setRangeMax(String rangeMax);

  @JsonProperty("binWidth")
  Integer getBinWidth();

  @JsonProperty("binWidth")
  void setBinWidth(Integer binWidth);

  @JsonProperty("binWidthOverride")
  Integer getBinWidthOverride();

  @JsonProperty("binWidthOverride")
  void setBinWidthOverride(Integer binWidthOverride);

  @JsonProperty("binUnits")
  BinUnits getBinUnits();

  @JsonProperty("binUnits")
  void setBinUnits(BinUnits binUnits);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
