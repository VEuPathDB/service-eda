package org.veupathdb.service.eda.merge

import org.veupathdb.service.eda.common.model.ReferenceMetadata
import org.veupathdb.service.eda.generated.model.APIFilter
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec
import org.veupathdb.service.eda.generated.model.VariableSpec
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariableFactory
import org.veupathdb.service.eda.merge.core.request.ComputeInfo

internal class MergeResources(
  val metadata: ReferenceMetadata,
  val derivedVariableSpecs: List<DerivedVariableSpec>,
  val derivedVariableFactory: DerivedVariableFactory,
  val subsetFilters: List<APIFilter>,
  val computeInfo: ComputeInfo?, // FIXME: this seems request related
  val targetEntityID: String,
  val outputVarSpecs: List<VariableSpec>,
)
