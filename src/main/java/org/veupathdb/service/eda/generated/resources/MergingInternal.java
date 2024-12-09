package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.BadRequestError;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponse;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.ServerError;
import org.veupathdb.service.eda.generated.model.UnprocessableEntityError;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/merging-internal")
public interface MergingInternal {
  @POST
  @Path("/query")
  @Produces({
      "text/tab-separated-values",
      "application/json"
  })
  @Consumes("application/json")
  PostMergingInternalQueryResponse postMergingInternalQuery(MergedEntityTabularPostRequest entity);

  class PostMergingInternalQueryResponse extends ResponseDelegate {
    private PostMergingInternalQueryResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostMergingInternalQueryResponse(Response response) {
      super(response);
    }

    public static PostMergingInternalQueryResponse respond200WithTextTabSeparatedValues(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/tab-separated-values");
      responseBuilder.entity(entity);
      return new PostMergingInternalQueryResponse(responseBuilder.build(), entity);
    }

    public static PostMergingInternalQueryResponse respond400WithApplicationJson(
        BadRequestError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(400).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostMergingInternalQueryResponse(responseBuilder.build(), entity);
    }

    public static PostMergingInternalQueryResponse respond422WithApplicationJson(
        UnprocessableEntityError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(422).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostMergingInternalQueryResponse(responseBuilder.build(), entity);
    }

    public static PostMergingInternalQueryResponse respond500WithApplicationJson(
        ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostMergingInternalQueryResponse(responseBuilder.build(), entity);
    }
  }
}
