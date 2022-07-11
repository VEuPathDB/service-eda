package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("category")
@JsonPropertyOrder({
    "id",
    "parentId",
    "providerLabel",
    "displayName",
    "definition",
    "displayType",
    "displayOrder",
    "isCategory",
    "type",
    "hideFrom"
})
public class APIVariablesCategoryImpl implements APIVariablesCategory {
  @JsonProperty("id")
  private String id;

  @JsonProperty("parentId")
  private String parentId;

  @JsonProperty("providerLabel")
  private String providerLabel;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("definition")
  private String definition;

  @JsonProperty("displayType")
  private APIVariableDisplayType displayType;

  @JsonProperty("displayOrder")
  private Long displayOrder;

  @JsonProperty("isCategory")
  private String isCategory;

  @JsonProperty("type")
  private final APIVariableType type = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("hideFrom")
  private List<String> hideFrom;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("parentId")
  public String getParentId() {
    return this.parentId;
  }

  @JsonProperty("parentId")
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  @JsonProperty("providerLabel")
  public String getProviderLabel() {
    return this.providerLabel;
  }

  @JsonProperty("providerLabel")
  public void setProviderLabel(String providerLabel) {
    this.providerLabel = providerLabel;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("definition")
  public String getDefinition() {
    return this.definition;
  }

  @JsonProperty("definition")
  public void setDefinition(String definition) {
    this.definition = definition;
  }

  @JsonProperty("displayType")
  public APIVariableDisplayType getDisplayType() {
    return this.displayType;
  }

  @JsonProperty("displayType")
  public void setDisplayType(APIVariableDisplayType displayType) {
    this.displayType = displayType;
  }

  @JsonProperty("displayOrder")
  public Long getDisplayOrder() {
    return this.displayOrder;
  }

  @JsonProperty("displayOrder")
  public void setDisplayOrder(Long displayOrder) {
    this.displayOrder = displayOrder;
  }

  @JsonProperty("isCategory")
  public String getIsCategory() {
    return this.isCategory;
  }

  @JsonProperty("isCategory")
  public void setIsCategory(String isCategory) {
    this.isCategory = isCategory;
  }

  @JsonProperty("type")
  public APIVariableType getType() {
    return this.type;
  }

  @JsonProperty("hideFrom")
  public List<String> getHideFrom() {
    return this.hideFrom;
  }

  @JsonProperty("hideFrom")
  public void setHideFrom(List<String> hideFrom) {
    this.hideFrom = hideFrom;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
