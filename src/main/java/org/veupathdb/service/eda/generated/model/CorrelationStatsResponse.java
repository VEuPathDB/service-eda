package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationStatsResponseImpl.class
)
public interface CorrelationStatsResponse {
  @JsonProperty("data1")
  List<String> getData1();

  @JsonProperty("data1")
  void setData1(List<String> data1);

  @JsonProperty("data2")
  List<String> getData2();

  @JsonProperty("data2")
  void setData2(List<String> data2);

  @JsonProperty("correlationCoef")
  List<String> getCorrelationCoef();

  @JsonProperty("correlationCoef")
  void setCorrelationCoef(List<String> correlationCoef);

  @JsonProperty("pValue")
  List<String> getPValue();

  @JsonProperty("pValue")
  void setPValue(List<String> pValue);

  @JsonProperty("adjustedPValue")
  List<String> getAdjustedPValue();

  @JsonProperty("adjustedPValue")
  void setAdjustedPValue(List<String> adjustedPValue);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
