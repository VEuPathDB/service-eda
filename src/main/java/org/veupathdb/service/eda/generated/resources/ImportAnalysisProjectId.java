package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.AnalysisListPostResponse;
import org.veupathdb.service.eda.generated.model.SingleAnalysisPublicInfo;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/import-analysis/{project-id}")
public interface ImportAnalysisProjectId {
  @GET
  @Path("/{analysis-id}")
  @Produces("application/json")
  GetImportAnalysisByProjectIdAndAnalysisIdResponse getImportAnalysisByProjectIdAndAnalysisId(
      @PathParam("project-id") String projectId, @PathParam("analysis-id") String analysisId);

  @GET
  @Path("/{analysis-id}/info")
  @Produces("application/json")
  GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse getImportAnalysisInfoByProjectIdAndAnalysisId(
      @PathParam("project-id") String projectId, @PathParam("analysis-id") String analysisId);

  class GetImportAnalysisByProjectIdAndAnalysisIdResponse extends ResponseDelegate {
    private GetImportAnalysisByProjectIdAndAnalysisIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetImportAnalysisByProjectIdAndAnalysisIdResponse(Response response) {
      super(response);
    }

    public static GetImportAnalysisByProjectIdAndAnalysisIdResponse respond200WithApplicationJson(
        AnalysisListPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetImportAnalysisByProjectIdAndAnalysisIdResponse(responseBuilder.build(), entity);
    }
  }

  class GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse extends ResponseDelegate {
    private GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse(Response response) {
      super(response);
    }

    public static GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse respond200WithApplicationJson(
        SingleAnalysisPublicInfo entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetImportAnalysisInfoByProjectIdAndAnalysisIdResponse(responseBuilder.build(), entity);
    }
  }
}
