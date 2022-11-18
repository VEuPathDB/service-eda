package org.veupathdb.service.eda.common.derivedvars.plugin;

import org.veupathdb.service.eda.common.model.DataRanges;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.Optional;

public interface DerivedVariable extends VariableSpec {

  EntityDef getEntity();

  APIVariableType getVariableType();

  APIVariableDataShape getVariableDataShape();

  default Optional<DataRanges> getDataRanges() {
    return Optional.empty();
  }
}
