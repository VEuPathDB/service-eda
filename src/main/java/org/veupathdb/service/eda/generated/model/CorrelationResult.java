package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationResultImpl.class
)
public interface CorrelationResult {
  @JsonProperty("var1")
  String getVar1();

  @JsonProperty("var1")
  void setVar1(String var1);

  @JsonProperty("var2")
  String getVar2();

  @JsonProperty("var2")
  void setVar2(String var2);

  @JsonProperty("correlationCoef")
  String getCorrelationCoef();

  @JsonProperty("correlationCoef")
  void setCorrelationCoef(String correlationCoef);

  @JsonProperty("pValue")
  String getPValue();

  @JsonProperty("pValue")
  void setPValue(String pValue);

  @JsonProperty("adjustedPValue")
  String getAdjustedPValue();

  @JsonProperty("adjustedPValue")
  void setAdjustedPValue(String adjustedPValue);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
