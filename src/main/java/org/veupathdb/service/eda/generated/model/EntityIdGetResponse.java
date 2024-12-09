package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = EntityIdGetResponseImpl.class
)
public interface EntityIdGetResponse {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("idColumnName")
  String getIdColumnName();

  @JsonProperty("idColumnName")
  void setIdColumnName(String idColumnName);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("displayNamePlural")
  String getDisplayNamePlural();

  @JsonProperty("displayNamePlural")
  void setDisplayNamePlural(String displayNamePlural);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("isManyToOneWithParent")
  Boolean getIsManyToOneWithParent();

  @JsonProperty("isManyToOneWithParent")
  void setIsManyToOneWithParent(Boolean isManyToOneWithParent);

  @JsonProperty("variables")
  List<APIVariable> getVariables();

  @JsonProperty("variables")
  void setVariables(List<APIVariable> variables);

  @JsonProperty("collections")
  List<APICollection> getCollections();

  @JsonProperty("collections")
  void setCollections(List<APICollection> collections);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
