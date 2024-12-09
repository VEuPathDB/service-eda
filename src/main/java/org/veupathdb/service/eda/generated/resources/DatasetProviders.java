package org.veupathdb.service.eda.generated.resources;

import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.DatasetProviderCreateRequest;
import org.veupathdb.service.eda.generated.model.DatasetProviderCreateResponse;
import org.veupathdb.service.eda.generated.model.DatasetProviderList;
import org.veupathdb.service.eda.generated.model.DatasetProviderPatch;
import org.veupathdb.service.eda.generated.support.PATCH;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/dataset-providers")
public interface DatasetProviders {
  @GET
  @Produces("application/json")
  GetDatasetProvidersResponse getDatasetProviders(@QueryParam("datasetId") String datasetId,
      @QueryParam("limit") @DefaultValue("1000") Long limit,
      @QueryParam("offset") @DefaultValue("0") Long offset);

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  PostDatasetProvidersResponse postDatasetProviders(DatasetProviderCreateRequest entity);

  @PATCH
  @Path("/{provider-id}")
  @Consumes("application/json")
  PatchDatasetProvidersByProviderIdResponse patchDatasetProvidersByProviderId(
      @PathParam("provider-id") Long providerId, List<DatasetProviderPatch> entity);

  @DELETE
  @Path("/{provider-id}")
  DeleteDatasetProvidersByProviderIdResponse deleteDatasetProvidersByProviderId(
      @PathParam("provider-id") Long providerId);

  class PostDatasetProvidersResponse extends ResponseDelegate {
    private PostDatasetProvidersResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostDatasetProvidersResponse(Response response) {
      super(response);
    }

    public static PostDatasetProvidersResponse respond200WithApplicationJson(
        DatasetProviderCreateResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostDatasetProvidersResponse(responseBuilder.build(), entity);
    }
  }

  class GetDatasetProvidersResponse extends ResponseDelegate {
    private GetDatasetProvidersResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetDatasetProvidersResponse(Response response) {
      super(response);
    }

    public static GetDatasetProvidersResponse respond200WithApplicationJson(
        DatasetProviderList entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetDatasetProvidersResponse(responseBuilder.build(), entity);
    }
  }

  class DeleteDatasetProvidersByProviderIdResponse extends ResponseDelegate {
    private DeleteDatasetProvidersByProviderIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private DeleteDatasetProvidersByProviderIdResponse(Response response) {
      super(response);
    }

    public static DeleteDatasetProvidersByProviderIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new DeleteDatasetProvidersByProviderIdResponse(responseBuilder.build());
    }
  }

  class PatchDatasetProvidersByProviderIdResponse extends ResponseDelegate {
    private PatchDatasetProvidersByProviderIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PatchDatasetProvidersByProviderIdResponse(Response response) {
      super(response);
    }

    public static PatchDatasetProvidersByProviderIdResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new PatchDatasetProvidersByProviderIdResponse(responseBuilder.build());
    }
  }
}
