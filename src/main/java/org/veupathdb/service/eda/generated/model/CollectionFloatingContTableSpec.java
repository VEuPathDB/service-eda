package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CollectionFloatingContTableSpecImpl.class
)
public interface CollectionFloatingContTableSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("xAxisVariable")
  CollectionOverlayConfigWithValues getXAxisVariable();

  @JsonProperty("xAxisVariable")
  void setXAxisVariable(CollectionOverlayConfigWithValues xAxisVariable);
}
