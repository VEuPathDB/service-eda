package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/ss-internal/studies/{study-id}/entities/{entity-id}")
public interface SsInternalStudiesStudyIdEntitiesEntityId {
  @POST
  @Path("/tabular")
  @Produces({
      "text/tab-separated-values",
      "application/json"
  })
  @Consumes("application/json")
  PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse postSsInternalStudiesEntitiesTabularByStudyIdAndEntityId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      EntityTabularPostRequest entity);

  class PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse extends ResponseDelegate {
    private PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse(Response response) {
      super(response);
    }

    public static PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse respond200WithTextTabSeparatedValues(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/tab-separated-values");
      responseBuilder.entity(entity);
      return new PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }

    public static PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse respond200WithApplicationJson(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostSsInternalStudiesEntitiesTabularByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }
  }
}
