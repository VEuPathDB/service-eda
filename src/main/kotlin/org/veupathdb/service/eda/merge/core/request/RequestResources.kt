package org.veupathdb.service.eda.merge.core.request

import org.gusdb.fgputil.validation.ValidationException
import org.veupathdb.service.eda.common.client.EdaSubsettingClient
import org.veupathdb.service.eda.common.model.ReferenceMetadata
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariable
import org.veupathdb.service.eda.merge.core.derivedvars.DerivedVariableFactory
import org.veupathdb.service.eda.util.logger
import kotlin.jvm.Throws

/**
 * Processes and validates incoming request data for tabular and derived
 * variable metadata requests.  This class is responsible for providing a
 * ReferenceMetadata which includes derived variables generated from the passed
 * derived variable specs.  It also provides a DerivedVariableFactory from which
 * plugin instances are delivered.
 * <p>
 * Note the superclass MergedTabularRequestResources, which supplements this
 * class with target entity, subset filters, and compute information needed for
 * tabular requests.
 */
open class RequestResources
@Throws(ValidationException::class)
constructor (
  studyId: String,
  variableSpecs: List<DerivedVariableSpec>,
) {
  private val logger = logger()

  val metadata = ReferenceMetadata(EdaSubsettingClient.getStudy(studyId))

  val derivedVariableSpecs = variableSpecs

  val derivedVariableFactory = DerivedVariableFactory(metadata, derivedVariableSpecs)

  init {
    val orderedDerivedVars: List<DerivedVariable> = derivedVariableFactory.allDerivedVars
    for (derivedVar in orderedDerivedVars) {
      logger.debug(
        "Validating depended vars of {} of type {}",
        derivedVar.columnName,
        derivedVar.functionName
      )

      // this call lets the plugins do additional setup where they can assume
      // depended var metadata is incorporated
      derivedVar.validateDependedVariables()

      // incorporate this derived variable
      metadata.incorporateDerivedVariable(derivedVar)
    }
  }
}
