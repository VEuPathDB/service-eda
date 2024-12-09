package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nodes",
    "links",
    "partitions"
})
public class BipartiteNetworkDataImpl implements BipartiteNetworkData {
  @JsonProperty("nodes")
  private List<NodeData> nodes;

  @JsonProperty("links")
  private List<LinkData> links;

  @JsonProperty("partitions")
  private List<NodeIdList> partitions;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("nodes")
  public List<NodeData> getNodes() {
    return this.nodes;
  }

  @JsonProperty("nodes")
  public void setNodes(List<NodeData> nodes) {
    this.nodes = nodes;
  }

  @JsonProperty("links")
  public List<LinkData> getLinks() {
    return this.links;
  }

  @JsonProperty("links")
  public void setLinks(List<LinkData> links) {
    this.links = links;
  }

  @JsonProperty("partitions")
  public List<NodeIdList> getPartitions() {
    return this.partitions;
  }

  @JsonProperty("partitions")
  public void setPartitions(List<NodeIdList> partitions) {
    this.partitions = partitions;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
