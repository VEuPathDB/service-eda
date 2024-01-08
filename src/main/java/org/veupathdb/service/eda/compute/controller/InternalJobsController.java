package org.veupathdb.service.eda.compute.controller;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.veupathdb.lib.compute.platform.AsyncPlatform;
import org.veupathdb.lib.compute.platform.job.InternalJobRecord;
import org.veupathdb.lib.container.jaxrs.server.annotations.AllowAdminAuth;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.lib.hash_id.HashID;
import org.veupathdb.service.eda.generated.model.InternalJob;
import org.veupathdb.service.eda.generated.model.InternalJobImpl;
import org.veupathdb.service.eda.generated.model.JobStatus;
import org.veupathdb.service.eda.generated.resources.InternalJobs;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Authenticated
@AllowAdminAuth(required = true)
public class InternalJobsController implements InternalJobs {

  @Override
  public GetInternalJobsResponse getInternalJobs() {
    // Expose internal jobs from async platform.
    final List<InternalJob> outputEntity = AsyncPlatform.listJobsInternal().stream()
        .map(this::toApiJob)
        .collect(Collectors.toList());
    return GetInternalJobsResponse.respond200WithApplicationJson(outputEntity);
  }

  @Override
  public GetInternalJobsByJobIdResponse getInternalJobsByJobId(String jobId) {
    if (jobId == null || jobId.isEmpty()) {
      throw new BadRequestException("JobID cannot be empty or null.");
    }
    final InternalJobRecord internalJobRecord = AsyncPlatform.getJobInternal(new HashID(jobId));
    if (internalJobRecord == null) {
      throw new NotFoundException("Could not find JobID with ID: " + jobId);
    }
    return GetInternalJobsByJobIdResponse.respond200WithApplicationJson(toApiJob(internalJobRecord));
  }

  /**
   * Convert an AsyncPlatform job to an internal job as modeled by the API.
   */
  private InternalJob toApiJob(InternalJobRecord jobRecord) {
    InternalJob internalJob = new InternalJobImpl();
    internalJob.setJobId(jobRecord.getJobID().getString());
    internalJob.setCreated(Date.from(jobRecord.getCreated().toInstant()));
    if (jobRecord.getFinished() != null) {
      internalJob.setFinished(Date.from(jobRecord.getFinished().toInstant()));
    }
    if (jobRecord.getGrabbed() != null) {
      internalJob.setGrabbed(Date.from(jobRecord.getGrabbed().toInstant()));
    }
    internalJob.setStatus(toApiJobStatus(jobRecord.getStatus()));
    return internalJob;
  }

  private JobStatus toApiJobStatus(org.veupathdb.lib.compute.platform.job.JobStatus jobStatus) {
    return Arrays.stream(JobStatus.values())
        .filter(status -> status != JobStatus.NOSUCHJOB) // This status is not stored in DB.
        .filter(status -> org.veupathdb.lib.compute.platform.job.JobStatus.fromString(status.value) == jobStatus)
        .findFirst()
        .orElseThrow();
  }
}
