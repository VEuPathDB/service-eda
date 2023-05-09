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
  GetJobsExpirationByStudyIdAndPluginNameAndAdminAuthKeyResponse getJobsExpirationByStudyIdAndPluginNameAndAdminAuthKey(
      @PathParam("study-id") String studyId, @PathParam("plugin-name") String pluginName,
      @PathParam("admin-auth-key") String adminAuthKey);

  class GetJobsExpirationByStudyIdAndPluginNameAndAdminAuthKeyResponse extends ResponseDelegate {
    private GetJobsExpirationByStudyIdAndPluginNameAndAdminAuthKeyResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private GetJobsExpirationByStudyIdAndPluginNameAndAdminAuthKeyResponse(Response response) {
      super(response);
    }

    public static GetJobsExpirationByStudyIdAndPluginNameAndAdminAuthKeyResponse respond200WithApplicationJson(
        ExpiredJobsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsExpirationByStudyIdAndPluginNameAndAdminAuthKeyResponse(responseBuilder.build(), entity);
    }
  }
}
