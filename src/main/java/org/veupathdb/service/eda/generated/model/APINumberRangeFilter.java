package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonTypeName("numberRange")
@JsonDeserialize(
    as = APINumberRangeFilterImpl.class
)
public interface APINumberRangeFilter extends APIFilter {
  APIFilterType _DISCRIMINATOR_TYPE_NAME = APIFilterType.NUMBERRANGE;

  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("type")
  APIFilterType getType();

  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("min")
  Number getMin();

  @JsonProperty("min")
  void setMin(Number min);

  @JsonProperty("max")
  Number getMax();

  @JsonProperty("max")
  void setMax(Number max);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
