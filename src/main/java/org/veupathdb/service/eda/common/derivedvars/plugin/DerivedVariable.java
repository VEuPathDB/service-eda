package org.veupathdb.service.eda.common.derivedvars.plugin;

import org.veupathdb.service.eda.common.model.DataRanges;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;
import java.util.Optional;

public interface DerivedVariable extends VariableSpec {

  EntityDef getEntity();

  List<VariableSpec> getRequiredInputVars();

  APIVariableType getVariableType();

  APIVariableDataShape getVariableDataShape();

  default Optional<DataRanges> getDataRanges() {
    return Optional.empty();
  }
}
