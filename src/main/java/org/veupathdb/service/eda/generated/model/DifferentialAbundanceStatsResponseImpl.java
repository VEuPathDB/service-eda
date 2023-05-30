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
    "log2foldChange",
    "pValue",
    "adjustedPValue",
    "pointId"
})
public class DifferentialAbundanceStatsResponseImpl implements DifferentialAbundanceStatsResponse {
  @JsonProperty("log2foldChange")
  private List<String> log2foldChange;

  @JsonProperty("pValue")
  private List<String> pValue;

  @JsonProperty("adjustedPValue")
  private List<String> adjustedPValue;

  @JsonProperty("pointId")
  private List<String> pointId;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("log2foldChange")
  public List<String> getLog2foldChange() {
    return this.log2foldChange;
  }

  @JsonProperty("log2foldChange")
  public void setLog2foldChange(List<String> log2foldChange) {
    this.log2foldChange = log2foldChange;
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
