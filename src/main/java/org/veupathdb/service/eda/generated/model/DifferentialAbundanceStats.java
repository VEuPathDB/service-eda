package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = DifferentialAbundanceStatsImpl.class
)
public interface DifferentialAbundanceStats {
  @JsonProperty("effectSize")
  List<String> getEffectSize();

  @JsonProperty("effectSize")
  void setEffectSize(List<String> effectSize);

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
