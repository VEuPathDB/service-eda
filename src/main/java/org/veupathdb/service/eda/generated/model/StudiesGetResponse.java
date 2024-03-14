package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = StudiesGetResponseImpl.class
)
public interface StudiesGetResponse {
  @JsonProperty("studies")
  List<APIStudyOverview> getStudies();

  @JsonProperty("studies")
  void setStudies(List<APIStudyOverview> studies);
}
