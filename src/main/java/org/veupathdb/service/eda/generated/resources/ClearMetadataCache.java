package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/clear-metadata-cache")
public interface ClearMetadataCache {
  @GET
  @Produces("text/plain")
  GetClearMetadataCacheResponse getClearMetadataCache();

  class GetClearMetadataCacheResponse extends ResponseDelegate {
    private GetClearMetadataCacheResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetClearMetadataCacheResponse(Response response) {
      super(response);
    }

    public static GetClearMetadataCacheResponse respond200WithTextPlain(Object entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "text/plain");
      responseBuilder.entity(entity);
      return new GetClearMetadataCacheResponse(responseBuilder.build(), entity);
    }
  }
}
