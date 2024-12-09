package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("nodeIds")
public class NodeIdListImpl implements NodeIdList {
  @JsonProperty("nodeIds")
  private List<String> nodeIds;

  @JsonProperty("nodeIds")
  public List<String> getNodeIds() {
    return this.nodeIds;
  }

  @JsonProperty("nodeIds")
  public void setNodeIds(List<String> nodeIds) {
    this.nodeIds = nodeIds;
  }
}
