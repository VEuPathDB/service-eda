package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("studies")
public class StudiesGetResponseImpl implements StudiesGetResponse {
  @JsonProperty("studies")
  private List<APIStudyOverview> studies;

  @JsonProperty("studies")
  public List<APIStudyOverview> getStudies() {
    return this.studies;
  }

  @JsonProperty("studies")
  public void setStudies(List<APIStudyOverview> studies) {
    this.studies = studies;
  }
}
