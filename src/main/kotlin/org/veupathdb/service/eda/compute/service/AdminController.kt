package org.veupathdb.service.eda.compute.service

import jakarta.ws.rs.core.StreamingOutput
import org.slf4j.LoggerFactory
import org.veupathdb.lib.container.jaxrs.providers.LogProvider
import org.veupathdb.lib.container.jaxrs.server.annotations.AdminRequired
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated
import org.veupathdb.lib.s3.s34k.S3Api
import org.veupathdb.lib.s3.s34k.S3Config
import org.veupathdb.lib.s3.s34k.fields.BucketName
import org.veupathdb.lib.s3.s34k.objects.S3Object
import org.veupathdb.service.eda.Main
import org.veupathdb.service.eda.generated.resources.AdminRPC

private const val RunningFlag: UByte = 1u
private const val CompletionFlag: UByte = 2u
private const val ExpiredFlag: UByte = 4u

private const val FlagInProgress = ".in-progress"
private const val FlagComplete = ".complete"
private const val FlagFailed = ".failed"
private const val FlagExpired = ".expired"

@Authenticated(adminOverride = Authenticated.AdminOverrideOption.ALLOW_ALWAYS)
@AdminRequired
class AdminController : AdminRPC {
  private val log = LoggerFactory.getLogger(javaClass)

  private val s3 = S3Api.newClient(S3Config(
    Main.config.s3Host,
    Main.config.s3Port.toUShort(),
    Main.config.s3UseHttps,
    Main.config.s3AccessToken,
    Main.config.s3SecretKey,
  ))
    .buckets[BucketName(Main.config.s3Bucket)]!!
    .objects

  override fun getAdminComputeListPossibleDeadWorkspaces(): AdminRPC.GetAdminComputeListPossibleDeadWorkspacesResponse {
    log.info("listing dead workspaces") // FIXME: remove this debug log line

    val workspaces = s3
      .listAll()
      .stream()
      .iterator()
      .asWorkspaces()
      .filter { it.hasRunningFlag && !(it.hasCompletionFlag || it.hasExpiredFlag) }
      .map { it.jobID }

    return AdminRPC.GetAdminComputeListPossibleDeadWorkspacesResponse.respond200WithTextPlain(StreamingOutput {
      val out = it.bufferedWriter()
      workspaces.forEach {
        out.write(it)
        out.newLine()
      }
    })
  }

  override fun deleteAdminComputeWorkspacesByJobId(jobId: String): AdminRPC.DeleteAdminComputeWorkspacesByJobIdResponse {
    val log = LogProvider.logger(javaClass)

    log.info("attempting to delete workspace contents for job {}", jobId)

    @Suppress("KotlinConstantConditions")
    s3.list(prefix = when {
      Main.S3_ROOT_PATH == "/"        -> jobId
      Main.S3_ROOT_PATH.endsWith("/") -> Main.S3_ROOT_PATH + jobId
      else                            -> Main.S3_ROOT_PATH + "/" + jobId
    }).forEach {
      log.info("deleting {}", it.path)
      it.delete()
    }

    return AdminRPC.DeleteAdminComputeWorkspacesByJobIdResponse.respond204()
  }

  private fun Iterator<S3Object>.asWorkspaces() = sequence {
    var state: UByte = 0u
    var jobID: String? = null

    while (hasNext()) {
      val it = next()
      val id = it.dirName()

      if (id != jobID) {
        if (jobID == null) {
          jobID = id
        } else {
          yield(Workspace(id, state))
          state = 0u
        }
      }

      when (it.baseName) {
        FlagInProgress -> state = state or RunningFlag
        FlagComplete   -> state = state or CompletionFlag
        FlagFailed     -> state = state or CompletionFlag
        FlagExpired    -> state = state or ExpiredFlag
      }
    }

    if (jobID != null)
      yield(Workspace(jobID, state))
  }

  private fun S3Object.dirName() = dirName.substringAfterLast('/')

  private data class Workspace(val jobID: String, val state: UByte) {
    inline val hasRunningFlag
      get() = state and RunningFlag == RunningFlag
    inline val hasCompletionFlag
      get() = state and CompletionFlag == CompletionFlag
    inline val hasExpiredFlag
      get() = state and ExpiredFlag == ExpiredFlag
  }
}
