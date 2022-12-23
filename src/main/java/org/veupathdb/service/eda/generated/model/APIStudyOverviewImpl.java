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
    "datasetId",
    "sha1hash",
    "sourceType",
    "displayName",
    "shortDisplayName",
    "description"
})
public class APIStudyOverviewImpl implements APIStudyOverview {
  @JsonProperty("id")
  private String id;

  @JsonProperty("datasetId")
  private String datasetId;

  @JsonProperty("sha1hash")
  private String sha1hash;

  @JsonProperty("sourceType")
  private StudySourceType sourceType;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("shortDisplayName")
  private String shortDisplayName;

  @JsonProperty("description")
  private String description;

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

  @JsonProperty("datasetId")
  public String getDatasetId() {
    return this.datasetId;
  }

  @JsonProperty("datasetId")
  public void setDatasetId(String datasetId) {
    this.datasetId = datasetId;
  }

  @JsonProperty("sha1hash")
  public String getSha1hash() {
    return this.sha1hash;
  }

  @JsonProperty("sha1hash")
  public void setSha1hash(String sha1hash) {
    this.sha1hash = sha1hash;
  }

  @JsonProperty("sourceType")
  public StudySourceType getSourceType() {
    return this.sourceType;
  }

  @JsonProperty("sourceType")
  public void setSourceType(StudySourceType sourceType) {
    this.sourceType = sourceType;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("shortDisplayName")
  public String getShortDisplayName() {
    return this.shortDisplayName;
  }

  @JsonProperty("shortDisplayName")
  public void setShortDisplayName(String shortDisplayName) {
    this.shortDisplayName = shortDisplayName;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
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
