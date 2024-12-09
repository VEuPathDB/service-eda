package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.HistoryResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/history")
public interface History {
  @GET
  @Produces("application/json")
  GetHistoryResponse getHistory(@QueryParam("limit") @DefaultValue("999") Long limit,
      @QueryParam("offset") @DefaultValue("0") Long offset);

  class GetHistoryResponse extends ResponseDelegate {
    private GetHistoryResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetHistoryResponse(Response response) {
      super(response);
    }

    public static GetHistoryResponse respond200WithApplicationJson(HistoryResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetHistoryResponse(responseBuilder.build(), entity);
    }
  }
}
