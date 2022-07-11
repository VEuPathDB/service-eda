package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("study")
public class StudyIdGetResponseImpl implements StudyIdGetResponse {
  @JsonProperty("study")
  private APIStudyDetail study;

  @JsonProperty("study")
  public APIStudyDetail getStudy() {
    return this.study;
  }

  @JsonProperty("study")
  public void setStudy(APIStudyDetail study) {
    this.study = study;
  }
}
