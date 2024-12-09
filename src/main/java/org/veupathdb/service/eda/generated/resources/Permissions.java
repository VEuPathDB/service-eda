package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.PermissionsGetResponse;
import org.veupathdb.service.eda.generated.model.StudyPermissionInfo;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/permissions")
public interface Permissions {
  @GET
  @Produces("application/json")
  GetPermissionsResponse getPermissions();

  @GET
  @Path("/{dataset-id}")
  @Produces("application/json")
  GetPermissionsByDatasetIdResponse getPermissionsByDatasetId(
      @PathParam("dataset-id") String datasetId);

  class GetPermissionsResponse extends ResponseDelegate {
    private GetPermissionsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetPermissionsResponse(Response response) {
      super(response);
    }

    public static GetPermissionsResponse respond200WithApplicationJson(
        PermissionsGetResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetPermissionsResponse(responseBuilder.build(), entity);
    }
  }

  class GetPermissionsByDatasetIdResponse extends ResponseDelegate {
    private GetPermissionsByDatasetIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetPermissionsByDatasetIdResponse(Response response) {
      super(response);
    }

    public static GetPermissionsByDatasetIdResponse respond200WithApplicationJson(
        StudyPermissionInfo entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetPermissionsByDatasetIdResponse(responseBuilder.build(), entity);
    }
  }
}
