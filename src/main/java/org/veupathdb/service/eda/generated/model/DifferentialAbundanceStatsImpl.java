package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "effectSize",
    "pValue",
    "adjustedPValue",
    "pointId"
})
public class DifferentialAbundanceStatsImpl implements DifferentialAbundanceStats {
  @JsonProperty("effectSize")
  private List<String> effectSize;

  @JsonProperty("pValue")
  private List<String> pValue;

  @JsonProperty("adjustedPValue")
  private List<String> adjustedPValue;

  @JsonProperty("pointId")
  private List<String> pointId;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("effectSize")
  public List<String> getEffectSize() {
    return this.effectSize;
  }

  @JsonProperty("effectSize")
  public void setEffectSize(List<String> effectSize) {
    this.effectSize = effectSize;
  }

  @JsonProperty("pValue")
  public List<String> getPValue() {
    return this.pValue;
  }

  @JsonProperty("pValue")
  public void setPValue(List<String> pValue) {
    this.pValue = pValue;
  }

  @JsonProperty("adjustedPValue")
  public List<String> getAdjustedPValue() {
    return this.adjustedPValue;
  }

  @JsonProperty("adjustedPValue")
  public void setAdjustedPValue(List<String> adjustedPValue) {
    this.adjustedPValue = adjustedPValue;
  }

  @JsonProperty("pointId")
  public List<String> getPointId() {
    return this.pointId;
  }

  @JsonProperty("pointId")
  public void setPointId(List<String> pointId) {
    this.pointId = pointId;
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
