package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = DifferentialExpressionPointImpl.class
)
public interface DifferentialExpressionPoint {
  @JsonProperty("effectSize")
  String getEffectSize();

  @JsonProperty("effectSize")
  void setEffectSize(String effectSize);

  @JsonProperty("pValue")
  String getPValue();

  @JsonProperty("pValue")
  void setPValue(String pValue);

  @JsonProperty("adjustedPValue")
  String getAdjustedPValue();

  @JsonProperty("adjustedPValue")
  void setAdjustedPValue(String adjustedPValue);

  @JsonProperty("pointId")
  String getPointId();

  @JsonProperty("pointId")
  void setPointId(String pointId);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
