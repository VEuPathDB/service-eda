package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "isUserStudy",
    "hasMap",
    "rootEntity"
})
public class APIStudyDetailImpl implements APIStudyDetail {
  @JsonProperty("id")
  private String id;

  @JsonProperty("isUserStudy")
  private Boolean isUserStudy;

  @JsonProperty("hasMap")
  private Boolean hasMap;

  @JsonProperty("rootEntity")
  private APIEntity rootEntity;

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

  @JsonProperty("isUserStudy")
  public Boolean getIsUserStudy() {
    return this.isUserStudy;
  }

  @JsonProperty("isUserStudy")
  public void setIsUserStudy(Boolean isUserStudy) {
    this.isUserStudy = isUserStudy;
  }

  @JsonProperty("hasMap")
  public Boolean getHasMap() {
    return this.hasMap;
  }

  @JsonProperty("hasMap")
  public void setHasMap(Boolean hasMap) {
    this.hasMap = hasMap;
  }

  @JsonProperty("rootEntity")
  public APIEntity getRootEntity() {
    return this.rootEntity;
  }

  @JsonProperty("rootEntity")
  public void setRootEntity(APIEntity rootEntity) {
    this.rootEntity = rootEntity;
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
