package org.veupathdb.service.eda.subset.service;

import jakarta.ws.rs.NotFoundException;
import org.gusdb.fgputil.Tuples;
import org.gusdb.fgputil.cache.ManagedMap;
import org.veupathdb.service.eda.generated.resources.TemporaryTabularResultTemporaryResultId;
import org.veupathdb.service.eda.subset.model.tabular.TabularResponses;

import java.util.Optional;
import java.util.UUID;

import static org.veupathdb.service.eda.subset.service.StudiesService.resolveSchema;

public class TemporaryTabularRequests implements TemporaryTabularResultTemporaryResultId {

  private static final ManagedMap<String, RequestBundle> REQUEST_MAP = new ManagedMap<>();

  public static String storeRequest(RequestBundle request) {
    String cacheKey = UUID.randomUUID().toString();
    REQUEST_MAP.put(cacheKey, request);
    return cacheKey;
  }

  private Optional<RequestBundle> retrieveRequest(String temporaryResultId) {
    if (REQUEST_MAP.containsKey(temporaryResultId)) {
      var value = REQUEST_MAP.get(temporaryResultId);
      REQUEST_MAP.remove(temporaryResultId);
      return Optional.of(value);
    }
    return Optional.empty();
  }

  @Override
  public GetTemporaryTabularResultByTemporaryResultIdResponse getTemporaryTabularResultByTemporaryResultId(String temporaryResultId) {

    RequestBundle request = retrieveRequest(temporaryResultId).orElseThrow(NotFoundException::new);
    TabularResponses.Type responseType = TabularResponses.Type.TABULAR;
    String dataSchema = resolveSchema(request.getStudy());

    return StudiesService.generateTabularRequest(request, responseType, dataSchema, (responseStream, type) ->
        GetTemporaryTabularResultByTemporaryResultIdResponse.respond200WithTextTabSeparatedValues(responseStream));
  }

}
