package org.veupathdb.service.eda.common.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MediaType;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.RequestFailure;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.functional.Either;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.APIFilter;
import org.veupathdb.service.eda.generated.model.APIStudyDetail;
import org.veupathdb.service.eda.generated.model.APIStudyOverview;
import org.veupathdb.service.eda.generated.model.EntityCountPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequest;
import org.veupathdb.service.eda.generated.model.EntityTabularPostRequestImpl;
import org.veupathdb.service.eda.generated.model.StudiesGetResponse;
import org.veupathdb.service.eda.generated.model.StudyIdGetResponse;
import org.veupathdb.service.eda.generated.model.VariableDistributionPostRequest;
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

  @Override
  public ResponseFuture getTabularDataStream(
      ReferenceMetadata metadata,
      List<APIFilter> subset,
      StreamSpec spec) throws ProcessingException {

    // build request object
    EntityTabularPostRequest request = new EntityTabularPostRequestImpl();
    request.setFilters(subset);
    request.setOutputVariableIds(spec.stream()
      // subsetting service only takes var IDs (must match entity requested, but should already be validated)
      .map(var -> var.getVariableId())
      .collect(Collectors.toList()));

    // build request url
    String url = getUrl("/studies/" + metadata.getStudyId() + "/entities/" + spec.getEntityId() + "/tabular");

    // make request
    return ClientUtil.makeAsyncPostRequest(url, request, "text/tabular");
  }

  //*****************************************************************
  //** Methods to support pass-through calls to subsetting service
  //*****************************************************************

  // GET request pass-throughs

  private Either<InputStream, RequestFailure> doGet(String resourcePath) throws Exception {
    return ClientUtil.makeAsyncGetRequest(getUrl(resourcePath), MediaType.APPLICATION_JSON).getEither();
  }

  public Either<InputStream, RequestFailure> getStudiesStream() throws Exception {
    return doGet("/studies");
  }

  public Either<InputStream, RequestFailure> getStudyStream(String studyId) throws Exception {
    return doGet("/studies/" + studyId);
  }

  public Either<InputStream, RequestFailure> getEntityStream(String studyId, String entityId) throws Exception {
    return doGet("/studies/" + studyId + "/entities/" + entityId);
  }

  // POST request pass-throughs

  private Either<InputStream, RequestFailure> doPost(String resourcePath, Object requestBodyObject) throws Exception {
    return ClientUtil.makeAsyncPostRequest(getUrl(resourcePath), requestBodyObject, MediaType.APPLICATION_JSON).getEither();
  }

  public Either<InputStream, RequestFailure> getEntityCountStream(String studyId, String entityId, EntityCountPostRequest requestObject) throws Exception {
    return doPost("/studies/" + studyId + "/entities/" + entityId + "/count", requestObject);
  }

  public Either<InputStream, RequestFailure> getVariableDistributionStream(String studyId, String entityId, String variableId, VariableDistributionPostRequest requestObject) throws Exception {
    return doPost("/studies/" + studyId + "/entities/" + entityId + "/variables/" + variableId + "/distribution", requestObject);
  }
}
