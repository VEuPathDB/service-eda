package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonTypeName("stringSet")
@JsonDeserialize(
    as = APIStringSetFilterImpl.class
)
public interface APIStringSetFilter extends APIFilter {
  APIFilterType _DISCRIMINATOR_TYPE_NAME = APIFilterType.STRINGSET;

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

  @JsonProperty("stringSet")
  List<String> getStringSet();

  @JsonProperty("stringSet")
  void setStringSet(List<String> stringSet);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
