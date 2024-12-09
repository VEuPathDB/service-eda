package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = APIStudyDetailImpl.class
)
public interface APIStudyDetail {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("isUserStudy")
  Boolean getIsUserStudy();

  @JsonProperty("isUserStudy")
  void setIsUserStudy(Boolean isUserStudy);

  @JsonProperty("hasMap")
  Boolean getHasMap();

  @JsonProperty("hasMap")
  void setHasMap(Boolean hasMap);

  @JsonProperty("rootEntity")
  APIEntity getRootEntity();

  @JsonProperty("rootEntity")
  void setRootEntity(APIEntity rootEntity);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
