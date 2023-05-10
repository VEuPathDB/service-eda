package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.ExpiredJobsResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/expire-compute-jobs")
public interface ExpireComputeJobs {
  @GET
  @Produces("application/json")
  GetExpireComputeJobsResponse getExpireComputeJobs(@QueryParam("job-id") String jobId,
      @QueryParam("study-id") String studyId, @QueryParam("plugin-name") String pluginName,
      @QueryParam("admin-auth-token") String adminAuthToken);

  class GetExpireComputeJobsResponse extends ResponseDelegate {
    private GetExpireComputeJobsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetExpireComputeJobsResponse(Response response) {
      super(response);
    }

    public static GetExpireComputeJobsResponse respond200WithApplicationJson(
        ExpiredJobsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetExpireComputeJobsResponse(responseBuilder.build(), entity);
    }
  }
}
