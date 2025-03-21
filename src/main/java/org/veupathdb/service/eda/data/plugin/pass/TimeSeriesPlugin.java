package org.veupathdb.service.eda.data.plugin.pass;

import java.util.List;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.generated.model.APIVariableType;

import static org.veupathdb.service.eda.data.metadata.AppsMetadata.CLINEPI_PROJECT;

public class TimeSeriesPlugin extends LineplotPlugin {

  @Override
  public String getDisplayName() {
    return "Time series";
  }

  @Override
  public String getDescription() {
    return "Visualize aggregate values of one variable across the sequential values of a temporal variable";
  }

  @Override
  public List<String> getProjects() {
    return List.of(CLINEPI_PROJECT);
  }
  
  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("yAxisVariable"), List.of("xAxisVariable", "overlayVariable", "facetVariable"))
      .pattern()
        .element("yAxisVariable")
          .types(APIVariableType.NUMBER, APIVariableType.DATE, APIVariableType.INTEGER)
          .description("Variable must be a number or date.")
        .element("xAxisVariable")
          .temporal(true)
          .description("Variable must be temporal and be of the same or a parent entity as the Y-axis variable.")
        .element("overlayVariable")
          .required(false)
          .maxValues(8)
          .description("Variable must have 8 or fewer unique values and be of the same or a parent entity as the X-axis variable.")
        .element("facetVariable")
          .required(false)
          .maxVars(2)
          .maxValues(10)
          .description("Variable(s) must have 10 or fewer unique values and be of the same or a parent entity as the Overlay variable.")
      .done();
  }
}
