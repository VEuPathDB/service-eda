package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIVariableWithValues.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIVariablesCategory.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIVariable.class)
})
@JsonDeserialize(
    as = APIVariableImpl.class
)
public interface APIVariable {
  APIVariableType _DISCRIMINATOR_TYPE_NAME = null;

  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("parentId")
  String getParentId();

  @JsonProperty("parentId")
  void setParentId(String parentId);

  @JsonProperty("providerLabel")
  String getProviderLabel();

  @JsonProperty("providerLabel")
  void setProviderLabel(String providerLabel);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("definition")
  String getDefinition();

  @JsonProperty("definition")
  void setDefinition(String definition);

  @JsonProperty("displayType")
  APIVariableDisplayType getDisplayType();

  @JsonProperty("displayType")
  void setDisplayType(APIVariableDisplayType displayType);

  @JsonProperty("displayOrder")
  Long getDisplayOrder();

  @JsonProperty("displayOrder")
  void setDisplayOrder(Long displayOrder);

  @JsonProperty("isCategory")
  String getIsCategory();

  @JsonProperty("isCategory")
  void setIsCategory(String isCategory);

  @JsonProperty("type")
  APIVariableType getType();

  @JsonProperty("hideFrom")
  List<String> getHideFrom();

  @JsonProperty("hideFrom")
  void setHideFrom(List<String> hideFrom);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
