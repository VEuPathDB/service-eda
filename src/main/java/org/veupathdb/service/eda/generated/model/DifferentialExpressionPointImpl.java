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
    "effectSize",
    "pValue",
    "adjustedPValue",
    "pointId"
})
public class DifferentialExpressionPointImpl implements DifferentialExpressionPoint {
  @JsonProperty("effectSize")
  private String effectSize;

  @JsonProperty("pValue")
  private String pValue;

  @JsonProperty("adjustedPValue")
  private String adjustedPValue;

  @JsonProperty("pointId")
  private String pointId;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("effectSize")
  public String getEffectSize() {
    return this.effectSize;
  }

  @JsonProperty("effectSize")
  public void setEffectSize(String effectSize) {
    this.effectSize = effectSize;
  }

  @JsonProperty("pValue")
  public String getPValue() {
    return this.pValue;
  }

  @JsonProperty("pValue")
  public void setPValue(String pValue) {
    this.pValue = pValue;
  }

  @JsonProperty("adjustedPValue")
  public String getAdjustedPValue() {
    return this.adjustedPValue;
  }

  @JsonProperty("adjustedPValue")
  public void setAdjustedPValue(String adjustedPValue) {
    this.adjustedPValue = adjustedPValue;
  }

  @JsonProperty("pointId")
  public String getPointId() {
    return this.pointId;
  }

  @JsonProperty("pointId")
  public void setPointId(String pointId) {
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
