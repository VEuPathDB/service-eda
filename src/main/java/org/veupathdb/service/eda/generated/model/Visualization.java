package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = VisualizationImpl.class
)
public interface Visualization {
  @JsonProperty("visualizationId")
  String getVisualizationId();

  @JsonProperty("visualizationId")
  void setVisualizationId(String visualizationId);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("descriptor")
  Object getDescriptor();

  @JsonProperty("descriptor")
  void setDescriptor(Object descriptor);
}
