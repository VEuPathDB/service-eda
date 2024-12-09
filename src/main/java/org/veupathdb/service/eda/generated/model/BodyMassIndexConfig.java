package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = BodyMassIndexConfigImpl.class
)
public interface BodyMassIndexConfig {
  @JsonProperty("heightVariable")
  VariableSpec getHeightVariable();

  @JsonProperty("heightVariable")
  void setHeightVariable(VariableSpec heightVariable);

  @JsonProperty("weightVariable")
  VariableSpec getWeightVariable();

  @JsonProperty("weightVariable")
  void setWeightVariable(VariableSpec weightVariable);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
