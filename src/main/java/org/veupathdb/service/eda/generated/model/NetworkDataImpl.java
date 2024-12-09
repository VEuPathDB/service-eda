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
    "links"
})
public class NetworkDataImpl implements NetworkData {
  @JsonProperty("nodes")
  private List<NodeData> nodes;

  @JsonProperty("links")
  private List<LinkData> links;

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

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
