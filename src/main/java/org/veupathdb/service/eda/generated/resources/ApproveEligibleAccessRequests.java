package org.veupathdb.service.eda.generated.resources;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.veupathdb.service.eda.generated.support.ResponseDelegate;

@Path("/approve-eligible-access-requests")
public interface ApproveEligibleAccessRequests {
  @POST
  PostApproveEligibleAccessRequestsResponse postApproveEligibleAccessRequests();

  class PostApproveEligibleAccessRequestsResponse extends ResponseDelegate {
    private PostApproveEligibleAccessRequestsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostApproveEligibleAccessRequestsResponse(Response response) {
      super(response);
    }

    public static PostApproveEligibleAccessRequestsResponse respond204() {
      Response.ResponseBuilder responseBuilder = Response.status(204);
      return new PostApproveEligibleAccessRequestsResponse(responseBuilder.build());
    }
  }
}
