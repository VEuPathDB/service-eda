package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = StudyIdGetResponseImpl.class
)
public interface StudyIdGetResponse {
  @JsonProperty("study")
  APIStudyDetail getStudy();

  @JsonProperty("study")
  void setStudy(APIStudyDetail study);
}
