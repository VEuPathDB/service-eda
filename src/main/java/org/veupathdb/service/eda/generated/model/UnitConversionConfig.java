package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = UnitConversionConfigImpl.class
)
public interface UnitConversionConfig {
  @JsonProperty("inputVariable")
  VariableSpec getInputVariable();

  @JsonProperty("inputVariable")
  void setInputVariable(VariableSpec inputVariable);

  @JsonProperty("outputUnits")
  String getOutputUnits();

  @JsonProperty("outputUnits")
  void setOutputUnits(String outputUnits);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
