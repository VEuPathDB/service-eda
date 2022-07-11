package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonTypeName("longitudeRange")
@JsonDeserialize(
    as = APILongitudeRangeFilterImpl.class
)
public interface APILongitudeRangeFilter extends APIFilter {
  APIFilterType _DISCRIMINATOR_TYPE_NAME = APIFilterType.LONGITUDERANGE;

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

  @JsonProperty("left")
  Number getLeft();

  @JsonProperty("left")
  void setLeft(Number left);

  @JsonProperty("right")
  Number getRight();

  @JsonProperty("right")
  void setRight(Number right);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
