package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "computedVariables",
    "computedCollections",
    "plotReferences"
})
public class ComputedVariableMetadataImpl implements ComputedVariableMetadata {
  @JsonProperty("computedVariables")
  private List<APIVariableWithValues> computedVariables;

  @JsonProperty("computedCollections")
  private List<APICollection> computedCollections;

  @JsonProperty("plotReferences")
  private List<PlotReference> plotReferences;

  @JsonProperty("computedVariables")
  public List<APIVariableWithValues> getComputedVariables() {
    return this.computedVariables;
  }

  @JsonProperty("computedVariables")
  public void setComputedVariables(List<APIVariableWithValues> computedVariables) {
    this.computedVariables = computedVariables;
  }

  @JsonProperty("computedCollections")
  public List<APICollection> getComputedCollections() {
    return this.computedCollections;
  }

  @JsonProperty("computedCollections")
  public void setComputedCollections(List<APICollection> computedCollections) {
    this.computedCollections = computedCollections;
  }

  @JsonProperty("plotReferences")
  public List<PlotReference> getPlotReferences() {
    return this.plotReferences;
  }

  @JsonProperty("plotReferences")
  public void setPlotReferences(List<PlotReference> plotReferences) {
    this.plotReferences = plotReferences;
  }
}
