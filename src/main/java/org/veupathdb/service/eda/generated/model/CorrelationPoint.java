package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationPointImpl.class
)
public interface CorrelationPoint {
  @JsonProperty("data1")
  String getData1();

  @JsonProperty("data1")
  void setData1(String data1);

  @JsonProperty("data2")
  String getData2();

  @JsonProperty("data2")
  void setData2(String data2);

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
