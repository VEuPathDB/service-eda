package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.EntityCountPostRequest;
import org.veupathdb.service.eda.generated.model.EntityCountPostResponse;
import org.veupathdb.service.eda.generated.model.EntityIdGetResponse;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponse;
import org.veupathdb.service.eda.generated.model.StudiesGetResponse;
import org.veupathdb.service.eda.generated.model.StudyIdGetResponse;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostRequest;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostResponse;
import org.veupathdb.service.eda.generated.model.VocabByRootEntityPostRequest;
import org.veupathdb.service.eda.generated.model.VocabByRootEntityPostResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/studies")
public interface Studies {
  @GET
  @Produces("application/json")
  GetStudiesResponse getStudies();

  @GET
  @Path("/{study-id}")
  @Produces("application/json")
  GetStudiesByStudyIdResponse getStudiesByStudyId(@PathParam("study-id") String studyId);

  @GET
  @Path("/{study-id}/entities/{entity-id}")
  @Produces("application/json")
  GetStudiesEntitiesByStudyIdAndEntityIdResponse getStudiesEntitiesByStudyIdAndEntityId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId);

  @POST
  @Path("/{study-id}/entities/{entity-id}/count")
  @Produces("application/json")
  @Consumes("application/json")
  PostStudiesEntitiesCountByStudyIdAndEntityIdResponse postStudiesEntitiesCountByStudyIdAndEntityId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      EntityCountPostRequest entity);

  @POST
  @Path("/{study-id}/entities/{entity-id}/tabular")
  @Produces({
      "text/tab-separated-values",
      "application/json"
  })
  @Consumes("application/json")
  PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse postStudiesEntitiesTabularByStudyIdAndEntityId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      EntityTabularPostRequest entity);

  @POST
  @Path("/{study-id}/entities/{entity-id}/tabular")
  @Produces({
      "text/tab-separated-values",
      "application/json"
  })
  @Consumes("application/x-www-form-urlencoded")
  PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse postStudiesEntitiesTabularByStudyIdAndEntityId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId);

  @POST
  @Path("/{study-id}/entities/{entity-id}/variables/{variable-id}/root-vocab")
  @Produces("text/tab-separated-values")
  @Consumes("application/json")
  PostStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableIdResponse postStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      @PathParam("variable-id") String variableId, VocabByRootEntityPostRequest entity);

  @POST
  @Path("/{study-id}/entities/{entity-id}/variables/{variable-id}/distribution")
  @Produces("application/json")
  @Consumes("application/json")
  PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse postStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableId(
      @PathParam("study-id") String studyId, @PathParam("entity-id") String entityId,
      @PathParam("variable-id") String variableId, VariableDistributionPostRequest entity);

  class GetStudiesResponse extends ResponseDelegate {
    private GetStudiesResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetStudiesResponse(Response response) {
      super(response);
    }

    public static GetStudiesResponse respond200WithApplicationJson(StudiesGetResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetStudiesResponse(responseBuilder.build(), entity);
    }
  }

  class GetStudiesByStudyIdResponse extends ResponseDelegate {
    private GetStudiesByStudyIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetStudiesByStudyIdResponse(Response response) {
      super(response);
    }

    public static GetStudiesByStudyIdResponse respond200WithApplicationJson(
        StudyIdGetResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetStudiesByStudyIdResponse(responseBuilder.build(), entity);
    }
  }

  class GetStudiesEntitiesByStudyIdAndEntityIdResponse extends ResponseDelegate {
    private GetStudiesEntitiesByStudyIdAndEntityIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetStudiesEntitiesByStudyIdAndEntityIdResponse(Response response) {
      super(response);
    }

    public static GetStudiesEntitiesByStudyIdAndEntityIdResponse respond200WithApplicationJson(
        EntityIdGetResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetStudiesEntitiesByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostStudiesEntitiesCountByStudyIdAndEntityIdResponse extends ResponseDelegate {
    private PostStudiesEntitiesCountByStudyIdAndEntityIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostStudiesEntitiesCountByStudyIdAndEntityIdResponse(Response response) {
      super(response);
    }

    public static PostStudiesEntitiesCountByStudyIdAndEntityIdResponse respond200WithApplicationJson(
        EntityCountPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostStudiesEntitiesCountByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse extends ResponseDelegate {
    private PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse(Response response) {
      super(response);
    }

    public static PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse respond200WithTextTabSeparatedValues(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/tab-separated-values");
      responseBuilder.entity(entity);
      return new PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }

    public static PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse respond200WithApplicationJson(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostStudiesEntitiesTabularByStudyIdAndEntityIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableIdResponse extends ResponseDelegate {
    private PostStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableIdResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableIdResponse(
        Response response) {
      super(response);
    }

    public static PostStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableIdResponse respond200WithTextTabSeparatedValues(
        VocabByRootEntityPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/tab-separated-values");
      responseBuilder.entity(entity);
      return new PostStudiesEntitiesVariablesRootVocabByStudyIdAndEntityIdAndVariableIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse extends ResponseDelegate {
    private PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse(
        Response response) {
      super(response);
    }

    public static PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse respond200WithApplicationJson(
        VariableDistributionPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostStudiesEntitiesVariablesDistributionByStudyIdAndEntityIdAndVariableIdResponse(responseBuilder.build(), entity);
    }
  }
}
