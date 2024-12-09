package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "displayName",
    "description",
    "projects",
    "computeName",
    "visualizations"
})
public class AppOverviewImpl implements AppOverview {
  @JsonProperty("name")
  private String name;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("projects")
  private List<String> projects;

  @JsonProperty("computeName")
  private String computeName;

  @JsonProperty("visualizations")
  private List<VisualizationOverview> visualizations;

  @JsonProperty("name")
  public String getName() {
    return this.name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("projects")
  public List<String> getProjects() {
    return this.projects;
  }

  @JsonProperty("projects")
  public void setProjects(List<String> projects) {
    this.projects = projects;
  }

  @JsonProperty("computeName")
  public String getComputeName() {
    return this.computeName;
  }

  @JsonProperty("computeName")
  public void setComputeName(String computeName) {
    this.computeName = computeName;
  }

  @JsonProperty("visualizations")
  public List<VisualizationOverview> getVisualizations() {
    return this.visualizations;
  }

  @JsonProperty("visualizations")
  public void setVisualizations(List<VisualizationOverview> visualizations) {
    this.visualizations = visualizations;
  }
}
