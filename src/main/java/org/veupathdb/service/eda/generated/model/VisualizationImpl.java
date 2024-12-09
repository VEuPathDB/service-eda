package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "visualizationId",
    "displayName",
    "descriptor"
})
public class VisualizationImpl implements Visualization {
  @JsonProperty("visualizationId")
  private String visualizationId;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("descriptor")
  private Object descriptor;

  @JsonProperty("visualizationId")
  public String getVisualizationId() {
    return this.visualizationId;
  }

  @JsonProperty("visualizationId")
  public void setVisualizationId(String visualizationId) {
    this.visualizationId = visualizationId;
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
}
