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
  @JsonProperty("var1")
  List<String> getVar1();

  @JsonProperty("var1")
  void setVar1(List<String> var1);

  @JsonProperty("var2")
  List<String> getVar2();

  @JsonProperty("var2")
  void setVar2(List<String> var2);

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
