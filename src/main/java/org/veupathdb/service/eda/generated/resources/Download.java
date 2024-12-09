package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.File;
import org.veupathdb.service.eda.generated.model.FileContentResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/download")
public interface Download {
  @GET
  @Path("/{project}/{study-id}")
  @Produces("application/json")
  GetDownloadByProjectAndStudyIdResponse getDownloadByProjectAndStudyId(
      @PathParam("project") String project, @PathParam("study-id") String studyId);

  @GET
  @Path("/{project}/{study-id}/{release}")
  @Produces("application/json")
  GetDownloadByProjectAndStudyIdAndReleaseResponse getDownloadByProjectAndStudyIdAndRelease(
      @PathParam("project") String project, @PathParam("study-id") String studyId,
      @PathParam("release") String release);

  @GET
  @Path("/{project}/{study-id}/{release}/{file}")
  @Produces("text/plain")
  GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse getDownloadByProjectAndStudyIdAndReleaseAndFile(
      @PathParam("project") String project, @PathParam("study-id") String studyId,
      @PathParam("release") String release, @PathParam("file") String file);

  class GetDownloadByProjectAndStudyIdResponse extends ResponseDelegate {
    private GetDownloadByProjectAndStudyIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetDownloadByProjectAndStudyIdResponse(Response response) {
      super(response);
    }

    public static GetDownloadByProjectAndStudyIdResponse respond200WithApplicationJson(
        List<String> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<String>> wrappedEntity = new GenericEntity<List<String>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetDownloadByProjectAndStudyIdResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetDownloadByProjectAndStudyIdAndReleaseResponse extends ResponseDelegate {
    private GetDownloadByProjectAndStudyIdAndReleaseResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetDownloadByProjectAndStudyIdAndReleaseResponse(Response response) {
      super(response);
    }

    public static GetDownloadByProjectAndStudyIdAndReleaseResponse respond200WithApplicationJson(
        List<File> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<File>> wrappedEntity = new GenericEntity<List<File>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetDownloadByProjectAndStudyIdAndReleaseResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse extends ResponseDelegate {
    private GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse(Response response) {
      super(response);
    }

    public static GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse respond200WithTextPlain(
        FileContentResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new GetDownloadByProjectAndStudyIdAndReleaseAndFileResponse(responseBuilder.build(), entity);
    }
  }
}
