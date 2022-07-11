package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "idColumnName",
    "displayName",
    "displayNamePlural",
    "description",
    "isManyToOneWithParent",
    "variables",
    "collections"
})
public class EntityIdGetResponseImpl implements EntityIdGetResponse {
  @JsonProperty("id")
  private String id;

  @JsonProperty("idColumnName")
  private String idColumnName;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("displayNamePlural")
  private String displayNamePlural;

  @JsonProperty("description")
  private String description;

  @JsonProperty("isManyToOneWithParent")
  private Boolean isManyToOneWithParent;

  @JsonProperty("variables")
  private List<APIVariable> variables;

  @JsonProperty("collections")
  private List<APICollection> collections;

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

  @JsonProperty("idColumnName")
  public String getIdColumnName() {
    return this.idColumnName;
  }

  @JsonProperty("idColumnName")
  public void setIdColumnName(String idColumnName) {
    this.idColumnName = idColumnName;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("displayNamePlural")
  public String getDisplayNamePlural() {
    return this.displayNamePlural;
  }

  @JsonProperty("displayNamePlural")
  public void setDisplayNamePlural(String displayNamePlural) {
    this.displayNamePlural = displayNamePlural;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("isManyToOneWithParent")
  public Boolean getIsManyToOneWithParent() {
    return this.isManyToOneWithParent;
  }

  @JsonProperty("isManyToOneWithParent")
  public void setIsManyToOneWithParent(Boolean isManyToOneWithParent) {
    this.isManyToOneWithParent = isManyToOneWithParent;
  }

  @JsonProperty("variables")
  public List<APIVariable> getVariables() {
    return this.variables;
  }

  @JsonProperty("variables")
  public void setVariables(List<APIVariable> variables) {
    this.variables = variables;
  }

  @JsonProperty("collections")
  public List<APICollection> getCollections() {
    return this.collections;
  }

  @JsonProperty("collections")
  public void setCollections(List<APICollection> collections) {
    this.collections = collections;
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
