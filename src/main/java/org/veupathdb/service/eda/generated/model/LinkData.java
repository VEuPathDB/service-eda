package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = LinkDataImpl.class
)
public interface LinkData {
  @JsonProperty("source")
  NodeData getSource();

  @JsonProperty("source")
  void setSource(NodeData source);

  @JsonProperty("target")
  NodeData getTarget();

  @JsonProperty("target")
  void setTarget(NodeData target);

  @JsonProperty("weight")
  String getWeight();

  @JsonProperty("weight")
  void setWeight(String weight);

  @JsonProperty("color")
  String getColor();

  @JsonProperty("color")
  void setColor(String color);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
