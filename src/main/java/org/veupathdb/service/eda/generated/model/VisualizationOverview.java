package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = VisualizationOverviewImpl.class
)
public interface VisualizationOverview {
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

  @JsonProperty("maxPanels")
  Number getMaxPanels();

  @JsonProperty("maxPanels")
  void setMaxPanels(Number maxPanels);

  @JsonProperty("dataElementConstraints")
  List<DataElementConstraintPattern> getDataElementConstraints();

  @JsonProperty("dataElementConstraints")
  void setDataElementConstraints(List<DataElementConstraintPattern> dataElementConstraints);

  @JsonProperty("dataElementDependencyOrder")
  List<List<String>> getDataElementDependencyOrder();

  @JsonProperty("dataElementDependencyOrder")
  void setDataElementDependencyOrder(List<List<String>> dataElementDependencyOrder);
}
