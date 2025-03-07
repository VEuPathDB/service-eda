package org.veupathdb.service.eda.data.plugin.standalonemap;

import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;

public class FloatingDateHistogramPlugin extends FloatingHistogramPlugin {

  @Override
  public String getDisplayName() {
    return "Timeline";
  }

  @Override
  public String getDescription() {
    return "Shows counts of entities vs. time.";
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("xAxisVariable", "overlayVariable"))
      .pattern()
        .element("xAxisVariable")
          .types(APIVariableType.DATE)
          .description("Variable must be a date.")
        .element("overlayVariable")
          .required(false)
      .done();
  }
}
