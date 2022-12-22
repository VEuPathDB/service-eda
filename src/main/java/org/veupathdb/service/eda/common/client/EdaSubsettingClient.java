package org.veupathdb.service.eda.common.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.ws.rs.ProcessingException;
import org.gusdb.fgputil.Tuples;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.web.MimeTypes;
import org.veupathdb.service.eda.common.client.spec.EdaSubsettingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.*;

import static org.gusdb.fgputil.functional.Functions.swallowAndGet;

public class EdaSubsettingClient extends StreamingDataClient {

  // request-scope cache for subsetting service metadata responses
  private List<String> _validStudyNameCache;
  private final Map<String, APIStudyDetail> _studyDetailCache = new HashMap<>();

  public EdaSubsettingClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    super(serviceBaseUrl, authHeader);
  }

  public List<String> getStudies() {
    return _validStudyNameCache != null ? _validStudyNameCache :
      (_validStudyNameCache = swallowAndGet(() -> ClientUtil
          .getResponseObject(getUrl("/studies"), StudiesGetResponse.class, getAuthHeaderMap()))
        .getStudies().stream().map(APIStudyOverview::getId).collect(Collectors.toList()));
  }

  /**
   * Returns the study detail for the study with the passed ID, or an empty optional
   * if no study exists for the passed ID.
   *
   * @param studyId id of a study
   * @return optional study detail for the found study
   */
  public Optional<APIStudyDetail> getStudy(String studyId) {
    if (!getStudies().contains(studyId)) return Optional.empty(); // invalid name
    if (!_studyDetailCache.containsKey(studyId)) {
      _studyDetailCache.put(studyId, swallowAndGet(() -> ClientUtil
          .getResponseObject(getUrl("/studies/" + studyId), StudyIdGetResponse.class, getAuthHeaderMap()).getStudy()));
    }
    return Optional.of(_studyDetailCache.get(studyId));
  }

  @Override
  public StreamSpecValidator getStreamSpecValidator() {
    return new EdaSubsettingSpecValidator();
  }

  @Override
  public String varToColumnHeader(VariableSpec var) {
    return var.getVariableId();
  }

  @Override
  public ResponseFuture getTabularDataStream(
      ReferenceMetadata metadata,
      List<APIFilter> subset,
      Optional<Tuples.TwoTuple<String,Object>> computeInfo, // should always be null; will be ignored
      StreamSpec spec) throws ProcessingException {

    // build request object
    EntityTabularPostRequest request = new EntityTabularPostRequestImpl();
    request.setFilters(subset);
    request.setOutputVariableIds(spec.stream()
      // subsetting service only takes var IDs (must match entity requested, but should already be validated)
      .map(VariableSpec::getVariableId)
      .collect(Collectors.toList()));

    // build request url using internal endpoint (does not check user permissions via data access service
    String url = getUrl("/ss-internal/studies/" + metadata.getStudyId() + "/entities/" + spec.getEntityId() + "/tabular");

    // make request
    return ClientUtil.makeAsyncPostRequest(url, request, MimeTypes.TEXT_TABULAR, getAuthHeaderMap());
  }

}
