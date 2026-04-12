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

  private static final ManagedMap<String, Tuples.TwoTuple<RequestBundle,TabularResponses.Type>> REQUEST_MAP = new ManagedMap<>();

  public static String storeRequest(RequestBundle request, TabularResponses.Type responseType) {
    String cacheKey = UUID.randomUUID().toString();
    REQUEST_MAP.put(cacheKey, new Tuples.TwoTuple<>(request, responseType));
    return cacheKey;
  }

  private Optional<Tuples.TwoTuple<RequestBundle,TabularResponses.Type>> retrieveRequest(String temporaryResultId) {
    if (REQUEST_MAP.containsKey(temporaryResultId)) {
      var value = REQUEST_MAP.get(temporaryResultId);
      REQUEST_MAP.remove(temporaryResultId);
      return Optional.of(value);
    }
    return Optional.empty();
  }

  @Override
  public GetTemporaryTabularResultByTemporaryResultIdResponse getTemporaryTabularResultByTemporaryResultId(String temporaryResultId) {

    Tuples.TwoTuple<RequestBundle,TabularResponses.Type> requestData = retrieveRequest(temporaryResultId).orElseThrow(NotFoundException::new);
    RequestBundle request = requestData.getFirst();
    TabularResponses.Type responseType = requestData.getSecond();
    String dataSchema = resolveSchema(request.getStudy());

    return StudiesService.generateTabularRequest(request, responseType, dataSchema, (responseStream, type) ->
      // ignore Accept sent by client and use the one submitted by the original "save" request
      type == TabularResponses.Type.JSON
        ? GetTemporaryTabularResultByTemporaryResultIdResponse.respond200WithApplicationJson(responseStream)
        : GetTemporaryTabularResultByTemporaryResultIdResponse.respond200WithTextTabSeparatedValues(responseStream));
  }

}
