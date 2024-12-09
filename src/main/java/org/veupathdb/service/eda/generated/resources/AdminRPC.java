package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/admin")
public interface AdminRPC {
  @GET
  @Path("/compute/list-possible-dead-workspaces")
  @Produces("text/plain")
  GetAdminComputeListPossibleDeadWorkspacesResponse getAdminComputeListPossibleDeadWorkspaces();

  @DELETE
  @Path("/compute/workspaces/{job-id}")
  DeleteAdminComputeWorkspacesByJobIdResponse deleteAdminComputeWorkspacesByJobId(
      @PathParam("job-id") String jobId);

  class GetAdminComputeListPossibleDeadWorkspacesResponse extends ResponseDelegate {
    private GetAdminComputeListPossibleDeadWorkspacesResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetAdminComputeListPossibleDeadWorkspacesResponse(Response response) {
      super(response);
    }

    public static GetAdminComputeListPossibleDeadWorkspacesResponse respond200WithTextPlain(
        Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new GetAdminComputeListPossibleDeadWorkspacesResponse(responseBuilder.build(), entity);
    }
  }

  class DeleteAdminComputeWorkspacesByJobIdResponse extends ResponseDelegate {
    private DeleteAdminComputeWorkspacesByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private DeleteAdminComputeWorkspacesByJobIdResponse(Response response) {
      super(response);
    }

    public static DeleteAdminComputeWorkspacesByJobIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new DeleteAdminComputeWorkspacesByJobIdResponse(responseBuilder.build());
    }
  }
}
