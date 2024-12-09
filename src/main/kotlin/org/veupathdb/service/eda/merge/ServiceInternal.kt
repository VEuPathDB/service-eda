package org.veupathdb.service.eda.merge

import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated
import org.veupathdb.lib.container.jaxrs.server.annotations.DisableJackson
import org.veupathdb.service.eda.generated.resources.MergingInternal

import org.veupathdb.service.eda.generated.model.MergedEntityTabularPostRequest as MergedTabularRequest
import org.veupathdb.service.eda.generated.resources.MergingInternal.PostMergingInternalQueryResponse as InternalQueryResponse

/**
 * Implementation of endpoints at or below the /merging-internal top-level
 * endpoint.  These endpoints are intended to be accessible only via the
 * internal docker network; therefore although authentication is present, not
 * dataset access restrictions are enforced.  The compute and data service
 * plugins have full access to the datasets so they can process the data down
 * into whatever product they produce.
 */
@Authenticated(allowGuests = true)
class ServiceInternal : MergingInternal {
  @DisableJackson
  override fun postMergingInternalQuery(requestBody: MergedTabularRequest): InternalQueryResponse {
    // no need to check perms; only internal clients can access this endpoint
    return InternalQueryResponse.respond200WithTextTabSeparatedValues(
      ServiceExternal.processMergedTabularRequest(requestBody)
    )
  }
}
