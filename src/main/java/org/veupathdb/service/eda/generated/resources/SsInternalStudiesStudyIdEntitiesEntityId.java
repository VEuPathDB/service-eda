package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.EntityCountPostRequest;
import org.veupathdb.service.eda.generated.model.EntityCountPostResponse;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponse;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostRequest;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/ss-internal/studies/{study-id}/entities/{entity-id}")
public interface SsInternalStudiesStudyIdEntitiesEntityId {
  @POST
  @Path("/count")
  @Produces("application/json")
  @Consumes("application/json")
  PostSsInternalStudiesEntitiesCountByStudyIdAndEntityIdResponse postSsInternalStudiesEntitiesCountByStudyIdAndEntityId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      EntityCountPostRequest entity);

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

  @POST
  @Path("/variables/{variable-id}/distribution")
  @Produces("application/json")
  @Consumes("application/json")
  PostSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse postSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      @PathParam("variable-id") String variableId, VariableDistributionPostRequest entity);

  class PostSsInternalStudiesEntitiesCountByStudyIdAndEntityIdResponse extends ResponseDelegate {
    private PostSsInternalStudiesEntitiesCountByStudyIdAndEntityIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostSsInternalStudiesEntitiesCountByStudyIdAndEntityIdResponse(Response response) {
      super(response);
    }

    public static PostSsInternalStudiesEntitiesCountByStudyIdAndEntityIdResponse respond200WithApplicationJson(
        EntityCountPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostSsInternalStudiesEntitiesCountByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }
  }

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

  class PostSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse extends ResponseDelegate {
    private PostSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse(
        Response response) {
      super(response);
    }

    public static PostSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse respond200WithApplicationJson(
        VariableDistributionPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostSsInternalStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse(responseBuilder.build(), entity);
    }
  }
}
