package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = DifferentialAbundanceStatsResponseImpl.class
)
public interface DifferentialAbundanceStatsResponse {
  @JsonProperty("log2foldChange")
  List<String> getLog2foldChange();

  @JsonProperty("log2foldChange")
  void setLog2foldChange(List<String> log2foldChange);

  @JsonProperty("pValue")
  List<String> getPValue();

  @JsonProperty("pValue")
  void setPValue(List<String> pValue);

  @JsonProperty("adjustedPValue")
  List<String> getAdjustedPValue();

  @JsonProperty("adjustedPValue")
  void setAdjustedPValue(List<String> adjustedPValue);

  @JsonProperty("pointId")
  List<String> getPointId();

  @JsonProperty("pointId")
  void setPointId(List<String> pointId);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
