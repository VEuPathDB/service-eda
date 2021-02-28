package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gusdb.fgputil.functional.Either;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
import org.veupathdb.service.eda.generated.model.APIStudyOverview;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.StudiesGetResponse;
import org.veupathdb.service.eda.generated.model.StudyIdGetResponse;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import static org.gusdb.fgputil.functional.Functions.swallowAndGet;

public class EdaSubsettingClient extends AbstractTabularDataClient {

  // request-scope cache for subsetting service metadata responses
  private List<String> _validStudyNameCache;
  private Map<String, APIStudyDetail> _studyDetailCache = new HashMap<>();

  public EdaSubsettingClient(String serviceBaseUrl) {
    super(serviceBaseUrl);
  }

  public List<String> getStudies() {
    return _validStudyNameCache != null ? _validStudyNameCache :
      (_validStudyNameCache = swallowAndGet(() -> ClientUtil
          .getResponseObject(getUrl("/studies"), StudiesGetResponse.class))
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
          .getResponseObject(getUrl("/studies/" + studyId), StudyIdGetResponse.class).getStudy()));
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

  public InputStream getTabularDataStream(
      String studyId,
      List<APIFilter> subset,
      StreamSpec spec) {

    // build request object
    EntityTabularPostRequest request = new EntityTabularPostRequestImpl();
    request.setFilters(subset);
    request.setOutputVariableIds(spec.stream()
      // subsetting service only takes var IDs (must match entity requested, but should already be validated)
      .map(var -> var.getVariableId())
      .collect(Collectors.toList()));

    // build request url
    String url = getUrl("/studies/" + studyId + "/entities/" + spec.getEntityId() + "/tabular");

    // make request
    Either<Optional<InputStream>, RequestFailure> result = ClientUtil
        .makePostRequest(url, request, "text/tabular");

    // handle result
    if (result.isLeft()) {
      return result.getLeft().orElseThrow(() ->
          new RuntimeException("Tabular request did not return a response body."));
    }
    throw new RuntimeException(result.getRight().toString());
  }
}
