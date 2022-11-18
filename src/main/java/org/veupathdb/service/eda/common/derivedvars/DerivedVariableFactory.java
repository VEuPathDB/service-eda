package org.veupathdb.service.eda.common.derivedvars;

import java.util.Collections;
import java.util.List;

import org.veupathdb.service.eda.common.derivedvars.plugin.DerivedVariable;
import org.veupathdb.service.eda.common.derivedvars.plugin.Reduction;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.derivedvars.plugin.Transform;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;

public class DerivedVariableFactory {

  private final ReferenceMetadata _metadata;

  public DerivedVariableFactory(ReferenceMetadata metadata) {
    _metadata = metadata;
  }

  public List<Transform> getTransforms(EntityDef targetEntity) {
    return Collections.emptyList();
  }

  public List<Reduction> getReductions(EntityDef sourceEntity, EntityDef targetEntity) {
    return Collections.emptyList();
  }

  // note: incoming list will be in dependency order; i.e. only later derived vars
  //       will depend on earlier derived vars (plus no circular dependencies);
  //       name will also be validated for uniqueness within study
  public List<DerivedVariable> createDerivedVariables(List<DerivedVariableSpec> derivedVariables) {
    return Collections.emptyList();
  }
}
