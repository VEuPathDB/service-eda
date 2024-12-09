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
import org.veupathdb.service.eda.generated.model.NewStaffRequest;
import org.veupathdb.service.eda.generated.model.NewStaffResponse;
import org.veupathdb.service.eda.generated.model.StaffList;
import org.veupathdb.service.eda.generated.model.StaffPatch;
import org.veupathdb.service.eda.generated.support.PATCH;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/staff")
public interface Staff {
  @GET
  @Produces("application/json")
  GetStaffResponse getStaff(@QueryParam("limit") @DefaultValue("1000") Long limit,
      @QueryParam("offset") @DefaultValue("0") Long offset);

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  PostStaffResponse postStaff(NewStaffRequest entity);

  @PATCH
  @Path("/{staff-id}")
  @Consumes("application/json")
  PatchStaffByStaffIdResponse patchStaffByStaffId(@PathParam("staff-id") Long staffId,
      List<StaffPatch> entity);

  @DELETE
  @Path("/{staff-id}")
  DeleteStaffByStaffIdResponse deleteStaffByStaffId(@PathParam("staff-id") Long staffId);

  class PostStaffResponse extends ResponseDelegate {
    private PostStaffResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostStaffResponse(Response response) {
      super(response);
    }

    public static PostStaffResponse respond200WithApplicationJson(NewStaffResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostStaffResponse(responseBuilder.build(), entity);
    }
  }

  class GetStaffResponse extends ResponseDelegate {
    private GetStaffResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetStaffResponse(Response response) {
      super(response);
    }

    public static GetStaffResponse respond200WithApplicationJson(StaffList entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetStaffResponse(responseBuilder.build(), entity);
    }
  }

  class DeleteStaffByStaffIdResponse extends ResponseDelegate {
    private DeleteStaffByStaffIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private DeleteStaffByStaffIdResponse(Response response) {
      super(response);
    }

    public static DeleteStaffByStaffIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new DeleteStaffByStaffIdResponse(responseBuilder.build());
    }
  }

  class PatchStaffByStaffIdResponse extends ResponseDelegate {
    private PatchStaffByStaffIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PatchStaffByStaffIdResponse(Response response) {
      super(response);
    }

    public static PatchStaffByStaffIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new PatchStaffByStaffIdResponse(responseBuilder.build());
    }
  }
}
