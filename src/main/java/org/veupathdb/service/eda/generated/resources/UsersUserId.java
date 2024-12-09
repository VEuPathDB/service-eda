package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.AnalysisDetail;
import org.veupathdb.service.eda.generated.model.AnalysisListPatchRequest;
import org.veupathdb.service.eda.generated.model.AnalysisListPostRequest;
import org.veupathdb.service.eda.generated.model.AnalysisListPostResponse;
import org.veupathdb.service.eda.generated.model.AnalysisSummary;
import org.veupathdb.service.eda.generated.model.DerivedVariableGetResponse;
import org.veupathdb.service.eda.generated.model.DerivedVariablePatchRequest;
import org.veupathdb.service.eda.generated.model.DerivedVariablePostRequest;
import org.veupathdb.service.eda.generated.model.DerivedVariablePostResponse;
import org.veupathdb.service.eda.generated.model.SingleAnalysisPatchRequest;
import org.veupathdb.service.eda.generated.support.PATCH;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/users/{user-id}")
public interface UsersUserId {
  @GET
  @Path("/preferences/{project-id}")
  @Produces("application/json")
  GetUsersPreferencesByUserIdAndProjectIdResponse getUsersPreferencesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId);

  @PUT
  @Path("/preferences/{project-id}")
  @Consumes("application/json")
  PutUsersPreferencesByUserIdAndProjectIdResponse putUsersPreferencesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      String entity);

  @GET
  @Path("/analyses/{project-id}")
  @Produces("application/json")
  GetUsersAnalysesByUserIdAndProjectIdResponse getUsersAnalysesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId);

  @POST
  @Path("/analyses/{project-id}")
  @Produces("application/json")
  @Consumes("application/json")
  PostUsersAnalysesByUserIdAndProjectIdResponse postUsersAnalysesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      AnalysisListPostRequest entity);

  @PATCH
  @Path("/analyses/{project-id}")
  @Consumes("application/json")
  PatchUsersAnalysesByUserIdAndProjectIdResponse patchUsersAnalysesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      AnalysisListPatchRequest entity);

  @GET
  @Path("/analyses/{project-id}/{analysis-id}")
  @Produces("application/json")
  GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse getUsersAnalysesByUserIdAndProjectIdAndAnalysisId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      @PathParam("analysis-id") String analysisId);

  @PATCH
  @Path("/analyses/{project-id}/{analysis-id}")
  @Consumes("application/json")
  PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse patchUsersAnalysesByUserIdAndProjectIdAndAnalysisId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      @PathParam("analysis-id") String analysisId, SingleAnalysisPatchRequest entity);

  @DELETE
  @Path("/analyses/{project-id}/{analysis-id}")
  DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse deleteUsersAnalysesByUserIdAndProjectIdAndAnalysisId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      @PathParam("analysis-id") String analysisId);

  @POST
  @Path("/analyses/{project-id}/{analysis-id}/copy")
  @Produces("application/json")
  PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse postUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      @PathParam("analysis-id") String analysisId);

  @GET
  @Path("/derived-variables/{project-id}")
  @Produces("application/json")
  GetUsersDerivedVariablesByUserIdAndProjectIdResponse getUsersDerivedVariablesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId);

  @POST
  @Path("/derived-variables/{project-id}")
  @Produces("application/json")
  @Consumes("application/json")
  PostUsersDerivedVariablesByUserIdAndProjectIdResponse postUsersDerivedVariablesByUserIdAndProjectId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      DerivedVariablePostRequest entity);

  @GET
  @Path("/derived-variables/{project-id}/{derived-variable-id}")
  @Produces("application/json")
  GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse getUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      @PathParam("derived-variable-id") String derivedVariableId);

  @PATCH
  @Path("/derived-variables/{project-id}/{derived-variable-id}")
  @Consumes("application/json")
  PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse patchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableId(
      @PathParam("user-id") String userId, @PathParam("project-id") String projectId,
      @PathParam("derived-variable-id") String derivedVariableId,
      DerivedVariablePatchRequest entity);

  class GetUsersPreferencesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private GetUsersPreferencesByUserIdAndProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetUsersPreferencesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static GetUsersPreferencesByUserIdAndProjectIdResponse respond200WithApplicationJson(
        String entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUsersPreferencesByUserIdAndProjectIdResponse(responseBuilder.build(), entity);
    }
  }

  class PutUsersPreferencesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private PutUsersPreferencesByUserIdAndProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PutUsersPreferencesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static PutUsersPreferencesByUserIdAndProjectIdResponse respond202() {
      Response.ResponseBuilder responseBuilder = Response.status(202);
      return new PutUsersPreferencesByUserIdAndProjectIdResponse(responseBuilder.build());
    }
  }

  class GetUsersAnalysesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private GetUsersAnalysesByUserIdAndProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetUsersAnalysesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static GetUsersAnalysesByUserIdAndProjectIdResponse respond200WithApplicationJson(
        List<AnalysisSummary> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<AnalysisSummary>> wrappedEntity = new GenericEntity<List<AnalysisSummary>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetUsersAnalysesByUserIdAndProjectIdResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class PatchUsersAnalysesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private PatchUsersAnalysesByUserIdAndProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PatchUsersAnalysesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static PatchUsersAnalysesByUserIdAndProjectIdResponse respond202() {
      Response.ResponseBuilder responseBuilder = Response.status(202);
      return new PatchUsersAnalysesByUserIdAndProjectIdResponse(responseBuilder.build());
    }
  }

  class PostUsersAnalysesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private PostUsersAnalysesByUserIdAndProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostUsersAnalysesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static PostUsersAnalysesByUserIdAndProjectIdResponse respond200WithApplicationJson(
        AnalysisListPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUsersAnalysesByUserIdAndProjectIdResponse(responseBuilder.build(), entity);
    }
  }

  class DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse extends ResponseDelegate {
    private DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(Response response) {
      super(response);
    }

    public static DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse respond202() {
      Response.ResponseBuilder responseBuilder = Response.status(202);
      return new DeleteUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(responseBuilder.build());
    }
  }

  class GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse extends ResponseDelegate {
    private GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(Response response) {
      super(response);
    }

    public static GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse respond200WithApplicationJson(
        AnalysisDetail entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(responseBuilder.build(), entity);
    }
  }

  class PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse extends ResponseDelegate {
    private PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(Response response) {
      super(response);
    }

    public static PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse respond202() {
      Response.ResponseBuilder responseBuilder = Response.status(202);
      return new PatchUsersAnalysesByUserIdAndProjectIdAndAnalysisIdResponse(responseBuilder.build());
    }
  }

  class PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse extends ResponseDelegate {
    private PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse(Response response) {
      super(response);
    }

    public static PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse respond200WithApplicationJson(
        AnalysisListPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUsersAnalysesCopyByUserIdAndProjectIdAndAnalysisIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostUsersDerivedVariablesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private PostUsersDerivedVariablesByUserIdAndProjectIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostUsersDerivedVariablesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond200WithApplicationJson(
        DerivedVariablePostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build(), entity);
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond400() {
      Response.ResponseBuilder responseBuilder = Response.status(400);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond401() {
      Response.ResponseBuilder responseBuilder = Response.status(401);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond403() {
      Response.ResponseBuilder responseBuilder = Response.status(403);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond404() {
      Response.ResponseBuilder responseBuilder = Response.status(404);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond422() {
      Response.ResponseBuilder responseBuilder = Response.status(422);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static PostUsersDerivedVariablesByUserIdAndProjectIdResponse respond500() {
      Response.ResponseBuilder responseBuilder = Response.status(500);
      return new PostUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }
  }

  class GetUsersDerivedVariablesByUserIdAndProjectIdResponse extends ResponseDelegate {
    private GetUsersDerivedVariablesByUserIdAndProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetUsersDerivedVariablesByUserIdAndProjectIdResponse(Response response) {
      super(response);
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdResponse respond200WithApplicationJson(
        List<DerivedVariableGetResponse> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<DerivedVariableGetResponse>> wrappedEntity = new GenericEntity<List<DerivedVariableGetResponse>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build(), wrappedEntity);
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdResponse respond401() {
      Response.ResponseBuilder responseBuilder = Response.status(401);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdResponse respond404() {
      Response.ResponseBuilder responseBuilder = Response.status(404);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdResponse respond500() {
      Response.ResponseBuilder responseBuilder = Response.status(500);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdResponse(responseBuilder.build());
    }
  }

  class PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse extends ResponseDelegate {
    private PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(
        Response response) {
      super(response);
    }

    public static PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond204(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond401(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(401);
      return new PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond403(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(403);
      return new PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond404(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(404);
      return new PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond422(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(422);
      return new PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond500(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(500);
      return new PatchUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }
  }

  class GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse extends ResponseDelegate {
    private GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(
        Response response, Object entity) {
      super(response, entity);
    }

    private GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(
        Response response) {
      super(response);
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond200WithApplicationJson(
        DerivedVariableGetResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build(), entity);
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond401(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(401);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond404(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(404);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }

    public static GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse respond500(
        ) {
      Response.ResponseBuilder responseBuilder = Response.status(500);
      return new GetUsersDerivedVariablesByUserIdAndProjectIdAndDerivedVariableIdResponse(responseBuilder.build());
    }
  }
}
