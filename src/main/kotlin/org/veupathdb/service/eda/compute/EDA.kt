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
import org.veupathdb.service.eda.compute.service.ServiceOptions
import org.veupathdb.service.eda.compute.util.JobIDs
import org.veupathdb.service.eda.compute.util.toAuthTuple
import org.veupathdb.service.eda.compute.util.toJobResponse
import org.veupathdb.service.eda.generated.model.APIFilter
import org.veupathdb.service.eda.generated.model.APIStudyDetail
import org.veupathdb.service.eda.generated.model.ComputeRequestBase
import org.veupathdb.service.eda.generated.model.JobResponse
import java.io.InputStream
import java.util.*

/**
 * EDA Project/Service Access Singleton
 *
 * Home location for single functions used to work with various EDA services
 * including the compute service.
 *
 *
 */
object EDA {

  private val Log = LogManager.getLogger(javaClass)

  /**
   * Returns the [StudyAccess] permissions object for the target [studyID] and
   * user described by the given [auth] header.
   *
   * @param studyID ID of the study for which the target user's permissions
   * should be fetched.
   *
   * @param auth Auth header sent in with the HTTP request describing the user
   * whose permissions should be fetched.
   *
   * @return `StudyAccess` permissions object describing the permissions the
   * target user has for the target study.
   */
  @JvmStatic
  fun getStudyPerms(studyID: String, auth: TwoTuple<String, String>): StudyAccess =
    DatasetAccessClient(ServiceOptions.datasetAccessHost, auth).getStudyAccess(studyID)

  /**
   * Fetches the [APIStudyDetail] information from the EDA Subsetting Service
   * for the given [studyID] if such a study exists.
   *
   * @param studyID ID of the study whose metadata should be retrieved.
   *
   * @param auth Auth header sent in with the job HTTP request.
   *
   * @return An [Optional] that will wrap the `APIStudyDetail` returned from the
   * EDA Subsetting Service, if such a study exists.  If the target study was
   * not found, the returned `Optional` will be empty.
   */
  @JvmStatic
  fun getAPIStudyDetail(studyID: String, auth: TwoTuple<String, String>): Optional<APIStudyDetail> =
    EdaSubsettingClient(ServiceOptions.edaSubsettingHost, auth).getStudy(studyID)

  /**
   * Fetches the [APIStudyDetail] information from the EDA Subsetting Service
   * for the given [studyID], throwing an exception if no such study exists.
   *
   * @param studyID ID of the study whose metadata should be retrieved.
   *
   * @param auth Auth header sent in with the job HTTP request.
   *
   * @param err Optional exception provider that will be used to get the
   * exception that will be thrown if the target study does not exist.
   *
   * @return The `APIStudyDetail` information returned from the EDA Subsetting
   * Service.
   *
   * @throws Exception Throws the exception returned by the given exception
   * provider ([err]).
   */
  @JvmStatic
  @JvmOverloads
  fun requireAPIStudyDetail(
    studyID: String,
    auth: TwoTuple<String, String>,
    err: (studyID: String) -> Exception = this::noStudyDetail
  ): APIStudyDetail = getAPIStudyDetail(studyID, auth).orElseThrow { err(studyID) }

  /**
   * Fetches tabular study data from the EDA Merge Service for the given params.
   *
   * @param refMeta TODO: What is this?
   *
   * @param filters TODO: What is this?
   *
   * @param spec TODO: What is this?
   *
   * @param auth Auth header sent in with the job HTTP request.
   *
   * @return An [InputStream] over the tabular data returned from the EDA Merge
   * Service.
   */
  @JvmStatic
  fun getMergeData(
    refMeta: ReferenceMetadata,
    filters: List<APIFilter>,
    spec: StreamSpec,
    auth: TwoTuple<String, String>
  ): InputStream =
    EdaMergingClient(ServiceOptions.edaMergeHost, auth)
      .getTabularDataStream(refMeta, filters, Optional.empty(), spec).inputStream


  /**
   * Submits a new compute job to the queue.
   *
   * @param plugin Plugin details.
   *
   * @param payload HTTP request containing the compute config.
   *
   * @param auth: Auth header sent in with the job HTTP request.
   *
   * @return A response describing the job that was created.
   */
  @JvmStatic
  fun <R : ComputeRequestBase> getOrSubmitComputeJob(
    plugin:  PluginMeta<R>,
    payload: R,
    auth:    TwoTuple<String, String>,
  ): JobResponse {
    // Serialize the http request to json
    val serial = Json.convert(payload)

    val jobID = JobIDs.of(plugin.urlSegment, serial)

    // If the job already exists, just return it.
    AsyncPlatform.getJob(jobID)?.let { return it.toJobResponse() }

    Log.info("Submitting job {} to the queue", jobID)

    // Build the rabbitmq message payload
    val jobPay = PluginJobPayload(plugin.urlSegment, serial, auth.toAuthTuple())

    // Submit the job
    AsyncPlatform.submitJob(plugin.targetQueue.queueName) {
      this.jobID  = jobID
      this.config = Json.convert(jobPay)
    }

    // Look up the job we just submitted
    return AsyncPlatform.getJob(jobID)!!.toJobResponse()
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
