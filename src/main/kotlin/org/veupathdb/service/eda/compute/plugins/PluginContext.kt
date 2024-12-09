package org.veupathdb.service.eda.compute.plugins

import org.veupathdb.service.eda.common.model.ReferenceMetadata
import org.veupathdb.service.eda.compute.exec.ComputeJobContext
import org.veupathdb.service.eda.compute.process.ComputeProcessBuilder
import org.veupathdb.service.eda.generated.model.APIStudyDetail
import org.veupathdb.service.eda.generated.model.ComputeRequestBase

/**
 * Plugin Execution Context
 *
 * Provides the context for a plugin execution.
 *
 * The execution context includes:
 *
 * * The original HTTP request that trigger the job to be queued.
 * * A handle on the local scratch workspace where files can be read/written.
 * * Additional context from the compute service about the current job.
 * * Accessors for:
 *     * The API study details
 *     * The Tabular merge data for the job
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 1.0.0
 */
interface PluginContext<R: ComputeRequestBase, C> {

  /**
   * Meta information about the current plugin.
   */
  val pluginMeta: PluginMeta<R>

  /**
   * Original HTTP request that was sent to the compute service to kick off the
   * current job.
   */
  val request: R

  /**
   * Job configuration.
   *
   * Alias for `request.config` or `getRequest().getConfig()`
   */
  val config: C

  /**
   * A handle on the workspace the plugin is being executed in.
   */
  val workspace: PluginWorkspace

  /**
   * Additional compute service context for the job being executed.
   */
  val jobContext: ComputeJobContext

  /**
   * API Study Details
   */
  val studyDetail: APIStudyDetail

  /**
   * Reference Metadata
   */
  val referenceMetadata: ReferenceMetadata

  /**
   * Returns a new [ComputeProcessBuilder] which may be used to construct an
   * external process execution and run that external process.
   *
   * @param command Name of/absolute path to the external binary or script that
   * will be executed.
   *
   * @return A new [ComputeProcessBuilder] instance.
   */
  fun processBuilder(command: String, vararg args: String): ComputeProcessBuilder
}
