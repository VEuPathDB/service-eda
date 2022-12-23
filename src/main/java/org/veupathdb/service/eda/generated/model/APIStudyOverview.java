package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = APIStudyOverviewImpl.class
)
public interface APIStudyOverview {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("datasetId")
  String getDatasetId();

  @JsonProperty("datasetId")
  void setDatasetId(String datasetId);

  @JsonProperty("sha1hash")
  String getSha1hash();

  @JsonProperty("sha1hash")
  void setSha1hash(String sha1hash);

  @JsonProperty("sourceType")
  StudySourceType getSourceType();

  @JsonProperty("sourceType")
  void setSourceType(StudySourceType sourceType);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("shortDisplayName")
  String getShortDisplayName();

  @JsonProperty("shortDisplayName")
  void setShortDisplayName(String shortDisplayName);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
