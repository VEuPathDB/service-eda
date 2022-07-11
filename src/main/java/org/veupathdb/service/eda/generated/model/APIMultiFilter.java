package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonTypeName("multiFilter")
@JsonDeserialize(
    as = APIMultiFilterImpl.class
)
public interface APIMultiFilter extends APIFilter {
  APIFilterType _DISCRIMINATOR_TYPE_NAME = APIFilterType.MULTIFILTER;

  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("type")
  APIFilterType getType();

  @JsonProperty("operation")
  APIBooleanOperationType getOperation();

  @JsonProperty("operation")
  void setOperation(APIBooleanOperationType operation);

  @JsonProperty("variableId")
  String getVariableId();

  @JsonProperty("variableId")
  void setVariableId(String variableId);

  @JsonProperty("subFilters")
  List<APIMultiFilterSubFilter> getSubFilters();

  @JsonProperty("subFilters")
  void setSubFilters(List<APIMultiFilterSubFilter> subFilters);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
