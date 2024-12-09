package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.model.ContinuousVariableMetadataPostRequest;
import org.veupathdb.service.eda.generated.model.ContinuousVariableMetadataPostResponse;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/filter-aware-metadata/continuous-variable")
public interface FilterAwareMetadataContinuousVariable {
  @POST
  @Produces("application/json")
  @Consumes("application/json")
  PostFilterAwareMetadataContinuousVariableResponse postFilterAwareMetadataContinuousVariable(
      ContinuousVariableMetadataPostRequest entity);

  class PostFilterAwareMetadataContinuousVariableResponse extends ResponseDelegate {
    private PostFilterAwareMetadataContinuousVariableResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostFilterAwareMetadataContinuousVariableResponse(Response response) {
      super(response);
    }

    public static PostFilterAwareMetadataContinuousVariableResponse respond200WithApplicationJson(
        ContinuousVariableMetadataPostResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostFilterAwareMetadataContinuousVariableResponse(responseBuilder.build(), entity);
    }
  }
}
