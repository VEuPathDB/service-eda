package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ComputationImpl.class
)
public interface Computation {
  @JsonProperty("computationId")
  String getComputationId();

  @JsonProperty("computationId")
  void setComputationId(String computationId);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("descriptor")
  Object getDescriptor();

  @JsonProperty("descriptor")
  void setDescriptor(Object descriptor);

  @JsonProperty("visualizations")
  List<Visualization> getVisualizations();

  @JsonProperty("visualizations")
  void setVisualizations(List<Visualization> visualizations);
}
