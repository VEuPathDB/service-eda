package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = AppOverviewImpl.class
)
public interface AppOverview {
  @JsonProperty("name")
  String getName();

  @JsonProperty("name")
  void setName(String name);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("projects")
  List<String> getProjects();

  @JsonProperty("projects")
  void setProjects(List<String> projects);

  @JsonProperty("computeName")
  String getComputeName();

  @JsonProperty("computeName")
  void setComputeName(String computeName);

  @JsonProperty("visualizations")
  List<VisualizationOverview> getVisualizations();

  @JsonProperty("visualizations")
  void setVisualizations(List<VisualizationOverview> visualizations);
}
