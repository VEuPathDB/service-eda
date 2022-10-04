package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.ExamplePluginRequest;
import org.veupathdb.service.eda.generated.model.JobResponse;
import org.veupathdb.service.eda.generated.model.PluginOverview;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/computes")
public interface Computes {
  @GET
  @Produces("application/json")
  GetComputesResponse getComputes();

  @POST
  @Path("/example")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesExampleResponse postComputesExample(ExamplePluginRequest entity);

  @POST
  @Path("/example/{file}")
  @Produces("text/plain")
  @Consumes("application/json")
  PostComputesExampleByFileResponse postComputesExampleByFile(@PathParam("file") String file,
      ExamplePluginRequest entity);

  @POST
  @Path("/betadiv")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesBetadivResponse postComputesBetadiv(BetaDivPluginRequest entity);

  @POST
  @Path("/betadiv/{file}")
  @Produces("text/plain")
  @Consumes("application/json")
  PostComputesBetadivByFileResponse postComputesBetadivByFile(@PathParam("file") String file,
      BetaDivPluginRequest entity);

  class GetComputesResponse extends ResponseDelegate {
    private GetComputesResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetComputesResponse(Response response) {
      super(response);
    }

    public static GetComputesResponse respond200WithApplicationJson(List<PluginOverview> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<PluginOverview>> wrappedEntity = new GenericEntity<List<PluginOverview>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetComputesResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class PostComputesExampleResponse extends ResponseDelegate {
    private PostComputesExampleResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesExampleResponse(Response response) {
      super(response);
    }

    public static PostComputesExampleResponse respond200WithApplicationJson(JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesExampleResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesExampleByFileResponse extends ResponseDelegate {
    private PostComputesExampleByFileResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesExampleByFileResponse(Response response) {
      super(response);
    }

    public static PostComputesExampleByFileResponse respond200WithTextPlain(Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new PostComputesExampleByFileResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesBetadivResponse extends ResponseDelegate {
    private PostComputesBetadivResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesBetadivResponse(Response response) {
      super(response);
    }

    public static PostComputesBetadivResponse respond200WithApplicationJson(JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesBetadivResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesBetadivByFileResponse extends ResponseDelegate {
    private PostComputesBetadivByFileResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesBetadivByFileResponse(Response response) {
      super(response);
    }

    public static PostComputesBetadivByFileResponse respond200WithTextPlain(Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new PostComputesBetadivByFileResponse(responseBuilder.build(), entity);
    }
  }
}
