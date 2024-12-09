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
    "maxPanels",
    "dataElementConstraints",
    "dataElementDependencyOrder"
})
public class VisualizationOverviewImpl implements VisualizationOverview {
  @JsonProperty("name")
  private String name;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("projects")
  private List<String> projects;

  @JsonProperty("maxPanels")
  private Number maxPanels;

  @JsonProperty("dataElementConstraints")
  private List<DataElementConstraintPattern> dataElementConstraints;

  @JsonProperty("dataElementDependencyOrder")
  private List<List<String>> dataElementDependencyOrder;

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

  @JsonProperty("maxPanels")
  public Number getMaxPanels() {
    return this.maxPanels;
  }

  @JsonProperty("maxPanels")
  public void setMaxPanels(Number maxPanels) {
    this.maxPanels = maxPanels;
  }

  @JsonProperty("dataElementConstraints")
  public List<DataElementConstraintPattern> getDataElementConstraints() {
    return this.dataElementConstraints;
  }

  @JsonProperty("dataElementConstraints")
  public void setDataElementConstraints(List<DataElementConstraintPattern> dataElementConstraints) {
    this.dataElementConstraints = dataElementConstraints;
  }

  @JsonProperty("dataElementDependencyOrder")
  public List<List<String>> getDataElementDependencyOrder() {
    return this.dataElementDependencyOrder;
  }

  @JsonProperty("dataElementDependencyOrder")
  public void setDataElementDependencyOrder(List<List<String>> dataElementDependencyOrder) {
    this.dataElementDependencyOrder = dataElementDependencyOrder;
  }
}
