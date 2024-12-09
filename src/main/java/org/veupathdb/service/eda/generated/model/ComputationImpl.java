package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "computationId",
    "displayName",
    "descriptor",
    "visualizations"
})
public class ComputationImpl implements Computation {
  @JsonProperty("computationId")
  private String computationId;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("descriptor")
  private Object descriptor;

  @JsonProperty("visualizations")
  private List<Visualization> visualizations;

  @JsonProperty("computationId")
  public String getComputationId() {
    return this.computationId;
  }

  @JsonProperty("computationId")
  public void setComputationId(String computationId) {
    this.computationId = computationId;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("descriptor")
  public Object getDescriptor() {
    return this.descriptor;
  }

  @JsonProperty("descriptor")
  public void setDescriptor(Object descriptor) {
    this.descriptor = descriptor;
  }

  @JsonProperty("visualizations")
  public List<Visualization> getVisualizations() {
    return this.visualizations;
  }

  @JsonProperty("visualizations")
  public void setVisualizations(List<Visualization> visualizations) {
    this.visualizations = visualizations;
  }
}
