package org.veupathdb.service.eda.compute.controller;

import jakarta.ws.rs.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.veupathdb.lib.compute.platform.AsyncPlatform;
import org.veupathdb.lib.compute.platform.job.AsyncJob;
import org.veupathdb.lib.compute.platform.job.JobFileReference;
import org.veupathdb.lib.compute.platform.job.JobStatus;
import org.veupathdb.lib.compute.platform.model.JobReference;
import org.veupathdb.lib.container.jaxrs.server.annotations.AdminRequired;
import org.veupathdb.lib.hash_id.HashID;
import org.veupathdb.service.eda.compute.jobs.ReservedFiles;
import org.veupathdb.service.eda.generated.model.ExpiredJobsResponse;
import org.veupathdb.service.eda.generated.model.ExpiredJobsResponseImpl;
import org.veupathdb.service.eda.generated.resources.ExpireComputeJobs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Responsible for expiring jobs on command.  Service takes two optional arguments:
 * <p>
 * - study ID: if passed, endpoint will only expire jobs run against this study
 * - plugin name: if passed, endpoint will only expire jobs run using this plugin
 * <p>
 * If neither argument is passed, a call to this endpoint will expire all job results
 */
@AdminRequired
public class ExpirationController implements ExpireComputeJobs {

  private static final Logger LOG = LogManager.getLogger(ExpirationController.class);

  @Override
  public GetExpireComputeJobsResponse getExpireComputeJobs(String jobId, String studyId, String pluginName, String adminAuthToken) {
    if (jobId != null && (studyId != null || pluginName != null)) {
      throw new BadRequestException("If job-id param is specified, study-id and plugin-name are not allowed.");
    }
    List<HashID> filteredJobIds = findJobs(Optional.ofNullable(jobId), Optional.ofNullable(studyId), Optional.ofNullable(pluginName));
    int numJobsExpired = manuallyExpireJobs(filteredJobIds);
    LOG.info("Expired {} jobs in response to the following expiration request: jobId = {}, studyId = {}, pluginName = {}", numJobsExpired, jobId, studyId, pluginName);
    ExpiredJobsResponse response = new ExpiredJobsResponseImpl();
    response.setNumJobsExpired(numJobsExpired);
    return GetExpireComputeJobsResponse.respond200WithApplicationJson(response);
  }

  @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "resource"})
  private List<HashID> findJobs(Optional<String> jobIdOption, Optional<String> studyIdOption, Optional<String> pluginNameOption) {
    ForkJoinPool customThreadPool = null;
    try {
      customThreadPool = new ForkJoinPool(10);
      return customThreadPool.submit(() ->

        AsyncPlatform.listJobReferences().parallelStream()

          // can only expire owned jobs
          .filter(JobReference::getOwned)

          // convert to job ID
          .map(JobReference::getJobID)

          // get job details
          .map(AsyncPlatform::getJob)

          // filter out jobs that no longer exist?  (race condition?)
          .filter(Objects::nonNull)

          // filter out already-expired jobs
          .filter(job -> JobStatus.Expired != job.getStatus())

          .map(AsyncJob::getJobID)

          // filter jobs by requested criteria
          .filter(jobId ->

            // if job ID specified, then match exactly if job IDs match
            // only need to look up config if criteria specified
            jobIdOption.map(s -> jobId.getString().equals(s))
              .orElseGet(() -> (studyIdOption.isEmpty() && pluginNameOption.isEmpty()) ||
                jobMatchesCriteria(jobId, studyIdOption, pluginNameOption))
          )

          // collect remaining job IDs
          .toList()
      ).get();
    }
    catch (ExecutionException e) {
      throw new RuntimeException("Could not handle job expiration request", e);
    }
    catch (InterruptedException e) {
      throw new RuntimeException("Expiration request interrupted before completion", e);
    }
    finally {
      if (customThreadPool != null) {
        customThreadPool.shutdown();
      }
    }
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private boolean jobMatchesCriteria(HashID jobId, Optional<String> studyIdOption, Optional<String> pluginNameOption) {
    // find the config file
    JobFileReference configFile = AsyncPlatform.getJobFile(jobId, ReservedFiles.InputConfig);
    if (configFile == null) {
      LOG.warn("Could not find job config file in non-expired job {}", jobId);
      return false;
    }
    // read the config file
    try (InputStream in = configFile.open()) {
      JSONObject json = new JSONObject(new String(in.readAllBytes()));
      String pluginName = json.getString("plugin");
      String studyId = json.getJSONObject("request").getString("studyId");
      boolean matchesPluginName = pluginNameOption.map(pluginName::equals).orElse(true);
      boolean matchesStudyId = studyIdOption.map(studyId::equals).orElse(true);
      return matchesPluginName && matchesStudyId;
    }
    catch (IOException e) {
      LOG.error("Could not open or parse job config file for job {}", jobId);
      return true;
    }
  }

  private int manuallyExpireJobs(List<HashID> filteredJobIds) {
    int count = 0;
    for (HashID id : filteredJobIds) {
      try {
        AsyncPlatform.expireJob(id);
        count++;
      }
      catch (Exception e) {
        LOG.error("Unable to expire Job with ID {}", id, e);
      }
    }
    return count;
  }

}
