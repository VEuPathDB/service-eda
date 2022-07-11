package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APINumberRangeFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIDateSetFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APILongitudeRangeFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIMultiFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIDateRangeFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIStringSetFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APINumberSetFilter.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIFilter.class)
})
@JsonDeserialize(
    as = APIFilterImpl.class
)
public interface APIFilter {
  APIFilterType _DISCRIMINATOR_TYPE_NAME = null;

  @JsonProperty("entityId")
  String getEntityId();

  @JsonProperty("entityId")
  void setEntityId(String entityId);

  @JsonProperty("type")
  APIFilterType getType();

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
