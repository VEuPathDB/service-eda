package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.AnalysisSummaryWithUser;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/public/analyses/{project-id}")
public interface PublicAnalysesProjectId {
  @GET
  @Produces("application/json")
  GetPublicAnalysesByProjectIdResponse getPublicAnalysesByProjectId(
      @PathParam("project-id") String projectId);

  class GetPublicAnalysesByProjectIdResponse extends ResponseDelegate {
    private GetPublicAnalysesByProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetPublicAnalysesByProjectIdResponse(Response response) {
      super(response);
    }

    public static GetPublicAnalysesByProjectIdResponse respond200WithApplicationJson(
        List<AnalysisSummaryWithUser> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<AnalysisSummaryWithUser>> wrappedEntity = new GenericEntity<List<AnalysisSummaryWithUser>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetPublicAnalysesByProjectIdResponse(responseBuilder.build(), wrappedEntity);
    }
  }
}
