package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "startDate",
    "endDate",
    "createdOrModifiedCounts"
})
public class UserAnalysisMetricsResponseImpl implements UserAnalysisMetricsResponse {
  @JsonProperty("startDate")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd"
  )
  private OffsetDateTime startDate;

  @JsonProperty("endDate")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd"
  )
  private OffsetDateTime endDate;

  @JsonProperty("createdOrModifiedCounts")
  private UserAnalysisCounts createdOrModifiedCounts;

  @JsonProperty("startDate")
  public OffsetDateTime getStartDate() {
    return this.startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  @JsonProperty("endDate")
  public OffsetDateTime getEndDate() {
    return this.endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  @JsonProperty("createdOrModifiedCounts")
  public UserAnalysisCounts getCreatedOrModifiedCounts() {
    return this.createdOrModifiedCounts;
  }

  @JsonProperty("createdOrModifiedCounts")
  public void setCreatedOrModifiedCounts(UserAnalysisCounts createdOrModifiedCounts) {
    this.createdOrModifiedCounts = createdOrModifiedCounts;
  }
}
