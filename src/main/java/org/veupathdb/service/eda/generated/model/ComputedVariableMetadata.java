package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = ComputedVariableMetadataImpl.class
)
public interface ComputedVariableMetadata {
  @JsonProperty("computedVariables")
  List<APIVariableWithValues> getComputedVariables();

  @JsonProperty("computedVariables")
  void setComputedVariables(List<APIVariableWithValues> computedVariables);

  @JsonProperty("computedCollections")
  List<APICollection> getComputedCollections();

  @JsonProperty("computedCollections")
  void setComputedCollections(List<APICollection> computedCollections);

  @JsonProperty("plotReferences")
  List<PlotReference> getPlotReferences();

  @JsonProperty("plotReferences")
  void setPlotReferences(List<PlotReference> plotReferences);
}
