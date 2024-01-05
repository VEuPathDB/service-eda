package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.InternalJob;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/internal-jobs")
public interface InternalJobs {
  @GET
  @Produces("application/json")
  GetInternalJobsResponse getInternalJobs(@QueryParam("job-id") String jobId,
      @QueryParam("study-id") String studyId, @QueryParam("plugin-name") String pluginName,
      @QueryParam("admin-auth-token") String adminAuthToken);

  @GET
  @Path("/{job-id}")
  @Produces("application/json")
  GetInternalJobsByJobIdResponse getInternalJobsByJobId(@PathParam("job-id") String jobId);

  class GetInternalJobsResponse extends ResponseDelegate {
    private GetInternalJobsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetInternalJobsResponse(Response response) {
      super(response);
    }

    public static GetInternalJobsResponse respond200WithApplicationJson(List<InternalJob> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<InternalJob>> wrappedEntity = new GenericEntity<List<InternalJob>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetInternalJobsResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetInternalJobsByJobIdResponse extends ResponseDelegate {
    private GetInternalJobsByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetInternalJobsByJobIdResponse(Response response) {
      super(response);
    }

    public static GetInternalJobsByJobIdResponse respond200WithApplicationJson(InternalJob entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetInternalJobsByJobIdResponse(responseBuilder.build(), entity);
    }
  }
}
