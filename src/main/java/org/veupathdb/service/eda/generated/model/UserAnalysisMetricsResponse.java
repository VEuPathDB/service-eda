package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.OffsetDateTime;

@JsonDeserialize(
    as = UserAnalysisMetricsResponseImpl.class
)
public interface UserAnalysisMetricsResponse {
  @JsonProperty("startDate")
  OffsetDateTime getStartDate();

  @JsonProperty("startDate")
  void setStartDate(OffsetDateTime startDate);

  @JsonProperty("endDate")
  OffsetDateTime getEndDate();

  @JsonProperty("endDate")
  void setEndDate(OffsetDateTime endDate);

  @JsonProperty("createdOrModifiedCounts")
  UserAnalysisCounts getCreatedOrModifiedCounts();

  @JsonProperty("createdOrModifiedCounts")
  void setCreatedOrModifiedCounts(UserAnalysisCounts createdOrModifiedCounts);
}
