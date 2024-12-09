package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CategoricalDistributionBinImpl.class
)
public interface CategoricalDistributionBin {
  @JsonProperty("label")
  String getLabel();

  @JsonProperty("label")
  void setLabel(String label);

  @JsonProperty("value")
  Number getValue();

  @JsonProperty("value")
  void setValue(Number value);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
