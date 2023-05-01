package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.ExpiredJobsResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/jobs-expiration")
public interface JobsExpiration {
  @GET
  @Produces("application/json")
  GetJobsExpirationByStudyIdAndPluginNameResponse getJobsExpirationByStudyIdAndPluginName(
      @PathParam("study-id") String studyId, @PathParam("plugin-name") String pluginName);

  class GetJobsExpirationByStudyIdAndPluginNameResponse extends ResponseDelegate {
    private GetJobsExpirationByStudyIdAndPluginNameResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetJobsExpirationByStudyIdAndPluginNameResponse(Response response) {
      super(response);
    }

    public static GetJobsExpirationByStudyIdAndPluginNameResponse respond200WithApplicationJson(
        ExpiredJobsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsExpirationByStudyIdAndPluginNameResponse(responseBuilder.build(), entity);
    }
  }
}
