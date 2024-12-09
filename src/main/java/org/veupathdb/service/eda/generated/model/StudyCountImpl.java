package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "studyId",
    "count"
})
public class StudyCountImpl implements StudyCount {
  @JsonProperty("studyId")
  private String studyId;

  @JsonProperty("count")
  private Integer count;

  @JsonProperty("studyId")
  public String getStudyId() {
    return this.studyId;
  }

  @JsonProperty("studyId")
  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  @JsonProperty("count")
  public Integer getCount() {
    return this.count;
  }

  @JsonProperty("count")
  public void setCount(Integer count) {
    this.count = count;
  }
}
