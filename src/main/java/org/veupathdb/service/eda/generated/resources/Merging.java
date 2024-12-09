package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.BadRequestError;
import org.veupathdb.service.eda.generated.model.DerivedVariableBulkMetadataRequest;
import org.veupathdb.service.eda.generated.model.DerivedVariableDocumentationRequest;
import org.veupathdb.service.eda.generated.model.DerivedVariableMetadata;
import org.veupathdb.service.eda.generated.model.EntityTabularPostResponse;
import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.ServerError;
import org.veupathdb.service.eda.generated.model.UnitConversionMetadataResponse;
import org.veupathdb.service.eda.generated.model.UnprocessableEntityError;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/merging")
public interface Merging {
  @POST
  @Path("/derived-variables/input-specs")
  @Consumes("application/json")
  PostMergingDerivedVariablesInputSpecsResponse postMergingDerivedVariablesInputSpecs(
      DerivedVariableDocumentationRequest entity);

  @POST
  @Path("/derived-variables/metadata/variables")
  @Produces("application/json")
  @Consumes("application/json")
  PostMergingDerivedVariablesMetadataVariablesResponse postMergingDerivedVariablesMetadataVariables(
      DerivedVariableBulkMetadataRequest entity);

  @GET
  @Path("/derived-variables/metadata/units")
  @Produces("application/json")
  GetMergingDerivedVariablesMetadataUnitsResponse getMergingDerivedVariablesMetadataUnits();

  @POST
  @Path("/query")
  @Produces({
      "text/tab-separated-values",
      "application/json"
  })
  @Consumes("application/json")
  PostMergingQueryResponse postMergingQuery(MergedEntityTabularPostRequest entity);

  class PostMergingDerivedVariablesInputSpecsResponse extends ResponseDelegate {
    private PostMergingDerivedVariablesInputSpecsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostMergingDerivedVariablesInputSpecsResponse(Response response) {
      super(response);
    }

    public static PostMergingDerivedVariablesInputSpecsResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new PostMergingDerivedVariablesInputSpecsResponse(responseBuilder.build());
    }
  }

  class PostMergingDerivedVariablesMetadataVariablesResponse extends ResponseDelegate {
    private PostMergingDerivedVariablesMetadataVariablesResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostMergingDerivedVariablesMetadataVariablesResponse(Response response) {
      super(response);
    }

    public static PostMergingDerivedVariablesMetadataVariablesResponse respond200WithApplicationJson(
        List<DerivedVariableMetadata> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<DerivedVariableMetadata>> wrappedEntity = new GenericEntity<List<DerivedVariableMetadata>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new PostMergingDerivedVariablesMetadataVariablesResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetMergingDerivedVariablesMetadataUnitsResponse extends ResponseDelegate {
    private GetMergingDerivedVariablesMetadataUnitsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetMergingDerivedVariablesMetadataUnitsResponse(Response response) {
      super(response);
    }

    public static GetMergingDerivedVariablesMetadataUnitsResponse respond200WithApplicationJson(
        UnitConversionMetadataResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetMergingDerivedVariablesMetadataUnitsResponse(responseBuilder.build(), entity);
    }
  }

  class PostMergingQueryResponse extends ResponseDelegate {
    private PostMergingQueryResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostMergingQueryResponse(Response response) {
      super(response);
    }

    public static PostMergingQueryResponse respond200WithTextTabSeparatedValues(
        EntityTabularPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/tab-separated-values");
      responseBuilder.entity(entity);
      return new PostMergingQueryResponse(responseBuilder.build(), entity);
    }

    public static PostMergingQueryResponse respond400WithApplicationJson(BadRequestError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(400).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostMergingQueryResponse(responseBuilder.build(), entity);
    }

    public static PostMergingQueryResponse respond422WithApplicationJson(
        UnprocessableEntityError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(422).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostMergingQueryResponse(responseBuilder.build(), entity);
    }

    public static PostMergingQueryResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostMergingQueryResponse(responseBuilder.build(), entity);
    }
  }
}
