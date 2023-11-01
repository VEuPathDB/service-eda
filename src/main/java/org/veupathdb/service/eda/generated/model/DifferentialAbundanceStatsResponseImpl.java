package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "effectSizeLabel",
    "statistics",
    "pvalueFloor",
    "adjustedPValueFloor"
})
public class DifferentialAbundanceStatsResponseImpl implements DifferentialAbundanceStatsResponse {
  @JsonProperty("effectSizeLabel")
  private String effectSizeLabel;

  @JsonProperty("statistics")
  private DifferentialAbundanceStats statistics;

  @JsonProperty("pvalueFloor")
  private String pvalueFloor;

  @JsonProperty("adjustedPValueFloor")
  private String adjustedPValueFloor;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("effectSizeLabel")
  public String getEffectSizeLabel() {
    return this.effectSizeLabel;
  }

  @JsonProperty("effectSizeLabel")
  public void setEffectSizeLabel(String effectSizeLabel) {
    this.effectSizeLabel = effectSizeLabel;
  }

  @JsonProperty("statistics")
  public DifferentialAbundanceStats getStatistics() {
    return this.statistics;
  }

  @JsonProperty("statistics")
  public void setStatistics(DifferentialAbundanceStats statistics) {
    this.statistics = statistics;
  }

  @JsonProperty("pvalueFloor")
  public String getPvalueFloor() {
    return this.pvalueFloor;
  }

  @JsonProperty("pvalueFloor")
  public void setPvalueFloor(String pvalueFloor) {
    this.pvalueFloor = pvalueFloor;
  }

  @JsonProperty("adjustedPValueFloor")
  public String getAdjustedPValueFloor() {
    return this.adjustedPValueFloor;
  }

  @JsonProperty("adjustedPValueFloor")
  public void setAdjustedPValueFloor(String adjustedPValueFloor) {
    this.adjustedPValueFloor = adjustedPValueFloor;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
