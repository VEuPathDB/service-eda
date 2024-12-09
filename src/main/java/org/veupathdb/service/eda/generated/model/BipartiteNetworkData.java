package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = BipartiteNetworkDataImpl.class
)
public interface BipartiteNetworkData extends NetworkData {
  @JsonProperty("nodes")
  List<NodeData> getNodes();

  @JsonProperty("nodes")
  void setNodes(List<NodeData> nodes);

  @JsonProperty("links")
  List<LinkData> getLinks();

  @JsonProperty("links")
  void setLinks(List<LinkData> links);

  @JsonProperty("partitions")
  List<NodeIdList> getPartitions();

  @JsonProperty("partitions")
  void setPartitions(List<NodeIdList> partitions);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
