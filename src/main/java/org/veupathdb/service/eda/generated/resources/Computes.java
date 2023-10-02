package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.AlphaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.BetaDivPluginRequest;
import org.veupathdb.service.eda.generated.model.CorrelationPluginRequest;
import org.veupathdb.service.eda.generated.model.CorrelationStatsResponse;
import org.veupathdb.service.eda.generated.model.DifferentialAbundancePluginRequest;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceStatsResponse;
import org.veupathdb.service.eda.generated.model.ExamplePluginRequest;
import org.veupathdb.service.eda.generated.model.JobResponse;
import org.veupathdb.service.eda.generated.model.PluginOverview;
import org.veupathdb.service.eda.generated.model.RankedAbundancePluginRequest;
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
  PostComputesExampleResponse postComputesExample(
      @QueryParam("autostart") @DefaultValue("true") Boolean autostart,
      ExamplePluginRequest entity);

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
  PostComputesBetadivResponse postComputesBetadiv(
      @QueryParam("autostart") @DefaultValue("true") Boolean autostart,
      BetaDivPluginRequest entity);

  @POST
  @Path("/betadiv/{file}")
  @Produces("text/plain")
  @Consumes("application/json")
  PostComputesBetadivByFileResponse postComputesBetadivByFile(@PathParam("file") String file,
      BetaDivPluginRequest entity);

  @POST
  @Path("/alphadiv")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesAlphadivResponse postComputesAlphadiv(
      @QueryParam("autostart") @DefaultValue("true") Boolean autostart,
      AlphaDivPluginRequest entity);

  @POST
  @Path("/alphadiv/{file}")
  @Produces("text/plain")
  @Consumes("application/json")
  PostComputesAlphadivByFileResponse postComputesAlphadivByFile(@PathParam("file") String file,
      AlphaDivPluginRequest entity);

  @POST
  @Path("/rankedabundance")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesRankedabundanceResponse postComputesRankedabundance(
      @QueryParam("autostart") @DefaultValue("true") Boolean autostart,
      RankedAbundancePluginRequest entity);

  @POST
  @Path("/rankedabundance/{file}")
  @Produces("text/plain")
  @Consumes("application/json")
  PostComputesRankedabundanceByFileResponse postComputesRankedabundanceByFile(
      @PathParam("file") String file, RankedAbundancePluginRequest entity);

  @POST
  @Path("/differentialabundance")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesDifferentialabundanceResponse postComputesDifferentialabundance(
      @QueryParam("autostart") @DefaultValue("true") Boolean autostart,
      DifferentialAbundancePluginRequest entity);

  @POST
  @Path("/differentialabundance/statistics")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesDifferentialabundanceStatisticsResponse postComputesDifferentialabundanceStatistics(
      DifferentialAbundancePluginRequest entity);

  @POST
  @Path("/correlationassaymetadata")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesCorrelationassaymetadataResponse postComputesCorrelationassaymetadata(
      @QueryParam("autostart") @DefaultValue("true") Boolean autostart,
      CorrelationPluginRequest entity);

  @POST
  @Path("/correlationassaymetadata/statistics")
  @Produces("application/json")
  @Consumes("application/json")
  PostComputesCorrelationassaymetadataStatisticsResponse postComputesCorrelationassaymetadataStatistics(
      CorrelationPluginRequest entity);

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

  class PostComputesAlphadivResponse extends ResponseDelegate {
    private PostComputesAlphadivResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesAlphadivResponse(Response response) {
      super(response);
    }

    public static PostComputesAlphadivResponse respond200WithApplicationJson(JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesAlphadivResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesAlphadivByFileResponse extends ResponseDelegate {
    private PostComputesAlphadivByFileResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesAlphadivByFileResponse(Response response) {
      super(response);
    }

    public static PostComputesAlphadivByFileResponse respond200WithTextPlain(Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new PostComputesAlphadivByFileResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesRankedabundanceResponse extends ResponseDelegate {
    private PostComputesRankedabundanceResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesRankedabundanceResponse(Response response) {
      super(response);
    }

    public static PostComputesRankedabundanceResponse respond200WithApplicationJson(
        JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesRankedabundanceResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesRankedabundanceByFileResponse extends ResponseDelegate {
    private PostComputesRankedabundanceByFileResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesRankedabundanceByFileResponse(Response response) {
      super(response);
    }

    public static PostComputesRankedabundanceByFileResponse respond200WithTextPlain(Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new PostComputesRankedabundanceByFileResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesDifferentialabundanceResponse extends ResponseDelegate {
    private PostComputesDifferentialabundanceResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesDifferentialabundanceResponse(Response response) {
      super(response);
    }

    public static PostComputesDifferentialabundanceResponse respond200WithApplicationJson(
        JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesDifferentialabundanceResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesDifferentialabundanceStatisticsResponse extends ResponseDelegate {
    private PostComputesDifferentialabundanceStatisticsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesDifferentialabundanceStatisticsResponse(Response response) {
      super(response);
    }

    public static PostComputesDifferentialabundanceStatisticsResponse respond200WithApplicationJson(
        DifferentialAbundanceStatsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesDifferentialabundanceStatisticsResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesCorrelationassaymetadataResponse extends ResponseDelegate {
    private PostComputesCorrelationassaymetadataResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostComputesCorrelationassaymetadataResponse(Response response) {
      super(response);
    }

    public static PostComputesCorrelationassaymetadataResponse respond200WithApplicationJson(
        JobResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesCorrelationassaymetadataResponse(responseBuilder.build(), entity);
    }
  }

  class PostComputesCorrelationassaymetadataStatisticsResponse extends ResponseDelegate {
    private PostComputesCorrelationassaymetadataStatisticsResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private PostComputesCorrelationassaymetadataStatisticsResponse(Response response) {
      super(response);
    }

    public static PostComputesCorrelationassaymetadataStatisticsResponse respond200WithApplicationJson(
        CorrelationStatsResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostComputesCorrelationassaymetadataStatisticsResponse(responseBuilder.build(), entity);
    }
  }
}
