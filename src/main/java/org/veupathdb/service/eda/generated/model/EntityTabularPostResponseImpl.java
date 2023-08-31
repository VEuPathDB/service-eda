package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("tabular")
public class EntityTabularPostResponseImpl implements EntityTabularPostResponse {
  @JsonProperty("tabular")
  private List<List<String>> tabular;

  @JsonProperty("tabular")
  public List<List<String>> getTabular() {
    return this.tabular;
  }

  @JsonProperty("tabular")
  public void setTabular(List<List<String>> tabular) {
    this.tabular = tabular;
  }
}
