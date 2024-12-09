package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "source",
    "target",
    "weight",
    "color"
})
public class LinkDataImpl implements LinkData {
  @JsonProperty("source")
  private NodeData source;

  @JsonProperty("target")
  private NodeData target;

  @JsonProperty("weight")
  private String weight;

  @JsonProperty("color")
  private String color;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("source")
  public NodeData getSource() {
    return this.source;
  }

  @JsonProperty("source")
  public void setSource(NodeData source) {
    this.source = source;
  }

  @JsonProperty("target")
  public NodeData getTarget() {
    return this.target;
  }

  @JsonProperty("target")
  public void setTarget(NodeData target) {
    this.target = target;
  }

  @JsonProperty("weight")
  public String getWeight() {
    return this.weight;
  }

  @JsonProperty("weight")
  public void setWeight(String weight) {
    this.weight = weight;
  }

  @JsonProperty("color")
  public String getColor() {
    return this.color;
  }

  @JsonProperty("color")
  public void setColor(String color) {
    this.color = color;
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
