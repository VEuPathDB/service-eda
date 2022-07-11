package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("counts")
public class EntityTabularPostResponseImpl implements EntityTabularPostResponse {
  @JsonProperty("counts")
  private List<List<String>> counts;

  @JsonProperty("counts")
  public List<List<String>> getCounts() {
    return this.counts;
  }

  @JsonProperty("counts")
  public void setCounts(List<List<String>> counts) {
    this.counts = counts;
  }
}
