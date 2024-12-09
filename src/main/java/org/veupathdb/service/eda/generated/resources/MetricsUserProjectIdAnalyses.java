package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.MetricsReportResponse;
import org.veupathdb.service.eda.generated.model.MetricsUserProjectIdAnalysesGetStudyType;
import org.veupathdb.service.eda.generated.model.UserAnalysisMetricsResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/metrics/user/{project-id}/analyses")
public interface MetricsUserProjectIdAnalyses {
  @GET
  @Produces("application/json")
  GetMetricsUserAnalysesByProjectIdResponse getMetricsUserAnalysesByProjectId(
      @PathParam("project-id") String projectId, @QueryParam("startDate") String startDate,
      @QueryParam("endDate") String endDate,
      @QueryParam("studyType") @DefaultValue("ALL") MetricsUserProjectIdAnalysesGetStudyType studyType);

  @GET
  @Path("/reports")
  @Produces("application/zip")
  GetMetricsUserAnalysesReportsByProjectIdResponse getMetricsUserAnalysesReportsByProjectId(
      @PathParam("project-id") String projectId, @QueryParam("reportMonth") String reportMonth);

  class GetMetricsUserAnalysesByProjectIdResponse extends ResponseDelegate {
    private GetMetricsUserAnalysesByProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetMetricsUserAnalysesByProjectIdResponse(Response response) {
      super(response);
    }

    public static GetMetricsUserAnalysesByProjectIdResponse respond200WithApplicationJson(
        UserAnalysisMetricsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetMetricsUserAnalysesByProjectIdResponse(responseBuilder.build(), entity);
    }
  }

  class GetMetricsUserAnalysesReportsByProjectIdResponse extends ResponseDelegate {
    private GetMetricsUserAnalysesReportsByProjectIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetMetricsUserAnalysesReportsByProjectIdResponse(Response response) {
      super(response);
    }

    public static GetMetricsUserAnalysesReportsByProjectIdResponse respond200WithApplicationZip(
        MetricsReportResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/zip");
      responseBuilder.entity(entity);
      return new GetMetricsUserAnalysesReportsByProjectIdResponse(responseBuilder.build(), entity);
    }
  }
}
