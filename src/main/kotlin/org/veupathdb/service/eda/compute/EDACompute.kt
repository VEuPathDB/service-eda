package org.veupathdb.service.eda.compute

import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.NotFoundException
import org.apache.logging.log4j.LogManager
import org.gusdb.fgputil.Tuples.TwoTuple
import org.veupathdb.lib.compute.platform.AsyncPlatform
import org.veupathdb.lib.compute.platform.job.JobFileReference
import org.veupathdb.lib.jackson.Json
import org.veupathdb.service.eda.common.auth.StudyAccess
import org.veupathdb.service.eda.common.client.DatasetAccessClient
import org.veupathdb.service.eda.common.client.EdaMergingClient
import org.veupathdb.service.eda.common.client.EdaSubsettingClient
import org.veupathdb.service.eda.common.client.spec.StreamSpec
import org.veupathdb.service.eda.common.model.ReferenceMetadata
import org.veupathdb.service.eda.compute.exec.PluginJobPayload
import org.veupathdb.service.eda.compute.plugins.PluginMeta
import org.veupathdb.service.eda.Main
import org.veupathdb.service.eda.compute.util.JobIDs
import org.veupathdb.service.eda.compute.util.toJobResponse
import org.veupathdb.service.eda.generated.model.*
import java.io.InputStream
import java.util.*
import org.veupathdb.lib.compute.platform.job.JobStatus as JS

/**
 * EDA Project/Service Access Singleton
 *
 * Home location for single functions used to work with various EDA services
 * including the compute service.
 *
 *
 */
object EDACompute {

  private val Log = LogManager.getLogger(javaClass)

  /**
   * Fetches tabular study data from the EDA Merge Service for the given params.
   *
   * @param refMeta reference metadata about the EDA study whose data is being computed
   *
   * @param filters set of filters to determine the current subset
   *
   * @param spec specification of tabular data stream needed from subsetting service (entity + vars)
   *
   * @return An [InputStream] over the tabular data returned from the EDA Merge
   * Service.
   */
  @JvmStatic
  fun getMergeData(
    refMeta: ReferenceMetadata,
    filters: List<APIFilter>,
    derivedVars: List<DerivedVariableSpec>,
    spec: StreamSpec
  ): InputStream =
    EdaMergingClient.getTabularDataStream(refMeta, filters, derivedVars, Optional.empty(), spec).inputStream

  /**
   * Submits a new compute job to the queue.
   *
   * @param plugin Plugin details.
   *
   * @param payload HTTP request containing the compute config.
   *
   * @param autostart: Whether to start a job that does not yet exist.
   *
   * @return A response describing the job that was created.
   */
  @JvmStatic
  fun <R : ComputeRequestBase> getOrSubmitComputeJob(
    plugin: PluginMeta<R>,
    payload: R,
    autostart: Boolean,
  ): JobResponse {

    // Create job ID by hashing plugin name and compute config
    val jobID = JobIDs.of(plugin.urlSegment, payload)

    // Lookup the job to see if it already exists
    val existingJob = AsyncPlatform.getJob(jobID)

    // If the job already exists
    if (existingJob != null)
      // And the status is not expired OR autostart is false
      if (existingJob.status != JS.Expired || !autostart)
        // Just return the job
        return existingJob.toJobResponse()

    // If we made it here, the job either does not exist, or exists and is
    // expired.

    // If autostart is true then submit the job for (re)execution
    if (autostart) {
      Log.info("Submitting job {} to the queue", jobID)

      // Build the rabbitmq message payload
      val jobPay = PluginJobPayload(plugin.urlSegment, Json.convert(payload))

      // Submit the job
      AsyncPlatform.submitJob(plugin.targetQueue.queueName) {
        this.jobID = jobID
        this.config = Json.convert(jobPay)
      }

      // Return the job ID with queued status (even though it may have moved to another status already)
      return JobResponseImpl().also {
        it.jobID = jobID.string
        it.status = JobStatus.QUEUED
      }
    }

    // Return the job ID with no-such-job status
    return JobResponseImpl().also {
      it.jobID = jobID.string
      it.status = JobStatus.NOSUCHJOB
    }
  }

  @JvmStatic
  fun getComputeJobFiles(plugin: PluginMeta<out ComputeRequestBase>, payload: ComputeRequestBase): List<JobFileReference> {
    val jobID = JobIDs.of(plugin.urlSegment, payload)

    // Get the target job (or throw 404) and ensure that it is finished (or
    // throw 403)
    (AsyncPlatform.getJob(jobID) ?: throw NotFoundException())
      .status
      .isFinished || throw ForbiddenException()

    return AsyncPlatform.getJobFiles(jobID)
  }


  @JvmStatic
  private fun noStudyDetail(studyID: String) =
    Exception("Could not get APIStudyDetail for study $studyID")
}
