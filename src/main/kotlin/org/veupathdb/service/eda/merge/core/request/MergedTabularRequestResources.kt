package org.veupathdb.service.eda.merge.core.request

import jakarta.ws.rs.BadRequestException
import org.gusdb.fgputil.iterator.CloseableIterator
import org.gusdb.fgputil.validation.ValidationException
import org.veupathdb.service.eda.common.client.EdaComputeClient
import org.veupathdb.service.eda.common.client.EdaSubsettingClient
import org.veupathdb.service.eda.common.client.spec.EdaMergingSpecValidator
import org.veupathdb.service.eda.common.client.spec.StreamSpec
import org.veupathdb.service.eda.compute.EDACompute
import org.veupathdb.service.eda.compute.jobs.ReservedFiles
import org.veupathdb.service.eda.compute.plugins.PluginMeta
import org.veupathdb.service.eda.compute.plugins.PluginRegistry
import org.veupathdb.service.eda.generated.model.*

/**
 * Subclass of RequestResources which supplements the superclass' resources with
 * target entity, subset filters, and compute information needed for tabular
 * requests.  This includes fetching compute job metadata, computed variable
 * metadata (incorporated into the ReferenceMetadata instance), and a method
 * which provides the actual computed variable tabular stream, all by querying
 * the compute service.
 */
class MergedTabularRequestResources
@Throws(ValidationException::class)
constructor(
  studyId: String,
  variableSpecs: List<DerivedVariableSpec>,

  val subsetFilters: List<APIFilter>,
  val computeInfo: ComputeInfo?,
  val targetEntityId: String,
  val outputVariableSpecs: List<VariableSpec>,
) : RequestResources(studyId, variableSpecs) {
  @Deprecated("for removal")
  lateinit var computeSvc: EdaComputeClient

  constructor(request: MergedEntityTabularPostRequest) : this(
    request.studyId,
    request.derivedVariables ?: emptyList(),
    request.filters ?: emptyList(),
    request.computeSpec?.let { ComputeInfo(
      it.computeName,
      EdaComputeClient.ComputeRequestBody(
        request.studyId,
        request.filters ?: emptyList(),
        request.derivedVariables ?: emptyList(),
        it.computeConfig
      )
    ) },
    request.entityId,
    request.outputVariables ?: emptyList(),
  )

  init {
    // incorporate computed metadata (if compute info present)
    incorporateCompute()

    // validation of incoming request, based specifically on the requested
    // entity done during spec creation
    validateIncomingRequest()
  }

  fun getSubsettingTabularStream(spec: StreamSpec): CloseableIterator<Map<String, String>> {
    // for derived var plugins, need to ensure filters overrides produce set of
    // rows which are a subset of the rows produced by the "global" filters.
    // Easiest way to do that is to simply combine the filters, resulting in an
    // intersection of the global subset and the overridden subset.
    return if (spec.filtersOverride.isPresent && subsetFilters.isNotEmpty()) {
      StreamSpec(spec.streamName, spec.entityId).apply {
        addAll(spec)
        setFiltersOverride(subsetFilters + spec.filtersOverride.get())
      }.let { EdaSubsettingClient.getTabularDataIterator(metadata.studyId, subsetFilters, it) }
    } else {
      EdaSubsettingClient.getTabularDataIterator(metadata.studyId, subsetFilters, spec)
    }
  }

  fun getComputeStreamIterator(): CloseableIterator<Map<String, String>> {
    if (computeInfo == null)
      throw IllegalStateException("Cannot get compute stream iterator if no compute is specified in request.")

    val file = EDACompute.getComputeJobFiles(
      PluginRegistry.get(computeInfo.computeName) as PluginMeta<out ComputeRequestBase>,
      computeInfo.requestBody,
    ).first { it.name == ReservedFiles.OutputTabular }

    return EdaComputeClient.getJobTabularIteratorOutput(
      metadata.getEntity(computeInfo.computeEntity).orElseThrow(),
      computeInfo.variables,
      metadata,
      file::open
    )
  }

  private fun incorporateCompute() {
    // if compute specified, check if compute results are available; throw if
    // not, get computed metadata if so
    computeInfo?.also {
      if (!computeSvc.isJobResultsAvailable(it.computeName, it.requestBody))
        throw BadRequestException("Compute results are not available for the requested job.")
      else
        it.setMetadata(computeSvc.getJobVariableMetadata(it.computeName, it.requestBody))

      metadata.incorporateComputedVariables(it.variables)
    }
  }

  @Throws(ValidationException::class)
  private fun validateIncomingRequest() {
    // create a stream spec from the request input and validate using merge svc
    // spec validator
    val requestSpec = StreamSpec("incoming", targetEntityId)
    requestSpec.addAll(outputVariableSpecs)

    EdaMergingSpecValidator()
      .validateStreamSpecs(listOf(requestSpec), metadata)
      .throwIfInvalid()

    // if compute was requested, make sure the computed entity is the same as,
    // or an ancestor of, the target entity of this request
    if (computeInfo != null) {
      if (
        targetEntityId != computeInfo.computeEntity
        && metadata.getAncestors(metadata.getEntity(targetEntityId).orElseThrow())
          .none { it.id == computeInfo.computeEntity }
      ) {
        // we don't perform reductions on computed vars so they must be on the target entity or an ancestor
        throw ValidationException("Entity of computed variable must be the same as, or ancestor of, the target entity")
      }
    }
  }
}
