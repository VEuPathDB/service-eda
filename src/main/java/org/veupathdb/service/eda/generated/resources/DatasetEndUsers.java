package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.ApprovalStatus;
import org.veupathdb.service.eda.generated.model.EndUser;
import org.veupathdb.service.eda.generated.model.EndUserCreateRequest;
import org.veupathdb.service.eda.generated.model.EndUserCreateResponse;
import org.veupathdb.service.eda.generated.model.EndUserList;
import org.veupathdb.service.eda.generated.model.EndUserPatch;
import org.veupathdb.service.eda.generated.support.PATCH;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/dataset-end-users")
public interface DatasetEndUsers {
  @GET
  @Produces("application/json")
  GetDatasetEndUsersResponse getDatasetEndUsers(@QueryParam("datasetId") String datasetId,
      @QueryParam("limit") @DefaultValue("100") Long limit,
      @QueryParam("offset") @DefaultValue("0") Long offset,
      @QueryParam("approval") ApprovalStatus approval);

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  PostDatasetEndUsersResponse postDatasetEndUsers(EndUserCreateRequest entity);

  @GET
  @Path("/{end-user-id}")
  @Produces("application/json")
  GetDatasetEndUsersByEndUserIdResponse getDatasetEndUsersByEndUserId(
      @PathParam("end-user-id") String endUserId);

  @DELETE
  @Path("/{end-user-id}")
  DeleteDatasetEndUsersByEndUserIdResponse deleteDatasetEndUsersByEndUserId(
      @PathParam("end-user-id") String endUserId);

  @PATCH
  @Path("/{end-user-id}")
  @Consumes("application/json")
  PatchDatasetEndUsersByEndUserIdResponse patchDatasetEndUsersByEndUserId(
      @PathParam("end-user-id") String endUserId, List<EndUserPatch> entity);

  class GetDatasetEndUsersResponse extends ResponseDelegate {
    private GetDatasetEndUsersResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetDatasetEndUsersResponse(Response response) {
      super(response);
    }

    public static GetDatasetEndUsersResponse respond200WithApplicationJson(EndUserList entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetDatasetEndUsersResponse(responseBuilder.build(), entity);
    }
  }

  class PostDatasetEndUsersResponse extends ResponseDelegate {
    private PostDatasetEndUsersResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostDatasetEndUsersResponse(Response response) {
      super(response);
    }

    public static PostDatasetEndUsersResponse respond200WithApplicationJson(
        EndUserCreateResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostDatasetEndUsersResponse(responseBuilder.build(), entity);
    }
  }

  class GetDatasetEndUsersByEndUserIdResponse extends ResponseDelegate {
    private GetDatasetEndUsersByEndUserIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetDatasetEndUsersByEndUserIdResponse(Response response) {
      super(response);
    }

    public static GetDatasetEndUsersByEndUserIdResponse respond200WithApplicationJson(
        EndUser entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetDatasetEndUsersByEndUserIdResponse(responseBuilder.build(), entity);
    }
  }

  class PatchDatasetEndUsersByEndUserIdResponse extends ResponseDelegate {
    private PatchDatasetEndUsersByEndUserIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PatchDatasetEndUsersByEndUserIdResponse(Response response) {
      super(response);
    }

    public static PatchDatasetEndUsersByEndUserIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new PatchDatasetEndUsersByEndUserIdResponse(responseBuilder.build());
    }
  }

  class DeleteDatasetEndUsersByEndUserIdResponse extends ResponseDelegate {
    private DeleteDatasetEndUsersByEndUserIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private DeleteDatasetEndUsersByEndUserIdResponse(Response response) {
      super(response);
    }

    public static DeleteDatasetEndUsersByEndUserIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new DeleteDatasetEndUsersByEndUserIdResponse(responseBuilder.build());
    }
  }
}
