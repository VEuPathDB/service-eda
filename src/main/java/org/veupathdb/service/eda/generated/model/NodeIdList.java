package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = NodeIdListImpl.class
)
public interface NodeIdList {
  @JsonProperty("nodeIds")
  List<String> getNodeIds();

  @JsonProperty("nodeIds")
  void setNodeIds(List<String> nodeIds);
}
