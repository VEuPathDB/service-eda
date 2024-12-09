package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("count")
public class MetricsReportResponseImpl implements MetricsReportResponse {
  @JsonProperty("count")
  private Integer count;

  @JsonProperty("count")
  public Integer getCount() {
    return this.count;
  }

  @JsonProperty("count")
  public void setCount(Integer count) {
    this.count = count;
  }
}
