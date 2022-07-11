package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.ForbiddenError;
import org.veupathdb.service.eda.generated.model.JobResponse;
import org.veupathdb.service.eda.generated.model.NotFoundError;
import org.veupathdb.service.eda.generated.model.ServerError;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/jobs")
public interface Jobs {
  @DELETE
  @Path("/{job-id}")
  @Produces("application/json")
  DeleteJobsByJobIdResponse deleteJobsByJobId(@PathParam("job-id") String jobId);

  @GET
  @Path("/{job-id}")
  @Produces("application/json")
  GetJobsByJobIdResponse getJobsByJobId(@PathParam("job-id") String jobId);

  @GET
  @Path("/{job-id}/files")
  @Produces("application/json")
  GetJobsFilesByJobIdResponse getJobsFilesByJobId(@PathParam("job-id") String jobId);

  @GET
  @Path("/{job-id}/files/{file-name}")
  @Produces({
      "application/json",
      "text/plain"
  })
  GetJobsFilesByJobIdAndFileNameResponse getJobsFilesByJobIdAndFileName(
      @PathParam("job-id") String jobId, @PathParam("file-name") String fileName);

  class GetJobsByJobIdResponse extends ResponseDelegate {
    private GetJobsByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetJobsByJobIdResponse(Response response) {
      super(response);
    }

    public static GetJobsByJobIdResponse respond200WithApplicationJson(JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetJobsByJobIdResponse respond404WithApplicationJson(NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetJobsByJobIdResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsByJobIdResponse(responseBuilder.build(), entity);
    }
  }

  class DeleteJobsByJobIdResponse extends ResponseDelegate {
    private DeleteJobsByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private DeleteJobsByJobIdResponse(Response response) {
      super(response);
    }

    public static DeleteJobsByJobIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new DeleteJobsByJobIdResponse(responseBuilder.build());
    }

    public static DeleteJobsByJobIdResponse respond404WithApplicationJson(NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new DeleteJobsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static DeleteJobsByJobIdResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new DeleteJobsByJobIdResponse(responseBuilder.build(), entity);
    }
  }

  class GetJobsFilesByJobIdResponse extends ResponseDelegate {
    private GetJobsFilesByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetJobsFilesByJobIdResponse(Response response) {
      super(response);
    }

    public static GetJobsFilesByJobIdResponse respond200WithApplicationJson(List<String> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<String>> wrappedEntity = new GenericEntity<List<String>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetJobsFilesByJobIdResponse(responseBuilder.build(), wrappedEntity);
    }

    public static GetJobsFilesByJobIdResponse respond403WithApplicationJson(ForbiddenError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsFilesByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetJobsFilesByJobIdResponse respond404WithApplicationJson(NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsFilesByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetJobsFilesByJobIdResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsFilesByJobIdResponse(responseBuilder.build(), entity);
    }
  }

  class GetJobsFilesByJobIdAndFileNameResponse extends ResponseDelegate {
    private GetJobsFilesByJobIdAndFileNameResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetJobsFilesByJobIdAndFileNameResponse(Response response) {
      super(response);
    }

    public static HeadersFor200 headersFor200() {
      return new HeadersFor200();
    }

    public static GetJobsFilesByJobIdAndFileNameResponse respond200WithTextPlain(Object entity,
        HeadersFor200 headers) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      headers.toResponseBuilder(responseBuilder);
      return new GetJobsFilesByJobIdAndFileNameResponse(responseBuilder.build(), entity);
    }

    public static GetJobsFilesByJobIdAndFileNameResponse respond403WithApplicationJson(
        ForbiddenError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsFilesByJobIdAndFileNameResponse(responseBuilder.build(), entity);
    }

    public static GetJobsFilesByJobIdAndFileNameResponse respond404WithApplicationJson(
        NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsFilesByJobIdAndFileNameResponse(responseBuilder.build(), entity);
    }

    public static GetJobsFilesByJobIdAndFileNameResponse respond500WithApplicationJson(
        ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetJobsFilesByJobIdAndFileNameResponse(responseBuilder.build(), entity);
    }

    public static class HeadersFor200 extends HeaderBuilderBase {
      private HeadersFor200() {
      }

      public HeadersFor200 withContentDisposition(final String p) {
        headerMap.put("Content-Disposition", String.valueOf(p));;
        return this;
      }
    }
  }
}
