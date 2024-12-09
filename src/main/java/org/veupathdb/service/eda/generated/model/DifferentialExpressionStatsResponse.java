package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = DifferentialExpressionStatsResponseImpl.class
)
public interface DifferentialExpressionStatsResponse {
  @JsonProperty("effectSizeLabel")
  String getEffectSizeLabel();

  @JsonProperty("effectSizeLabel")
  void setEffectSizeLabel(String effectSizeLabel);

  @JsonProperty("statistics")
  List<DifferentialExpressionPoint> getStatistics();

  @JsonProperty("statistics")
  void setStatistics(List<DifferentialExpressionPoint> statistics);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
