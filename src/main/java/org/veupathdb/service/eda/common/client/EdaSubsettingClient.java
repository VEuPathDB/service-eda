package org.veupathdb.service.eda.common.client;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.web.MimeTypes;
import org.veupathdb.service.eda.common.client.spec.EdaSubsettingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

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
      List<APIFilter> defaultSubset,
      StreamSpec spec) throws ProcessingException {

    // build request object
    EntityTabularPostRequest request = new EntityTabularPostRequestImpl();
    request.setFilters(spec.getFiltersOverride().orElse(defaultSubset));
    request.setOutputVariableIds(spec.stream()
      // subsetting service only takes var IDs (must match entity requested, but should already be validated)
      .map(VariableSpec::getVariableId)
      .collect(Collectors.toList()));

    // build request url using internal endpoint (does not check user permissions via data access service)
    String url = getUrl("/ss-internal/studies/" + metadata.getStudyId() + "/entities/" + spec.getEntityId() + "/tabular");

    // make request
    return ClientUtil.makeAsyncPostRequest(url, request, MimeTypes.TEXT_TABULAR, getAuthHeaderMap());
  }

  public long getSubsetCount(
      ReferenceMetadata metadata,
      String entityId,
      List<APIFilter> subsetFilters
  ) {
    // validate entity ID against this study
    EntityDef entity = metadata.getEntity(entityId).orElseThrow();

    // build request object
    EntityCountPostRequest request = new EntityCountPostRequestImpl();
    request.setFilters(subsetFilters);

    // build request url using internal endpoint (does not check user permissions via data access service)
    String url = getUrl("/ss-internal/studies/" + metadata.getStudyId() + "/entities/" + entity.getId() + "/count");

    // make request
    ResponseFuture response = ClientUtil.makeAsyncPostRequest(url, request, MediaType.APPLICATION_JSON, getAuthHeaderMap());

    // parse output and return
    try (InputStream responseBody = response.getInputStream()) {
      EntityCountPostResponse responseObj = JsonUtil.Jackson.readValue(responseBody, EntityCountPostResponse.class);
      return responseObj.getCount();
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to complete subset count request.", e);
    }
  }

  public VariableDistributionPostResponse getCategoricalDistribution(
      ReferenceMetadata metadata,
      VariableSpec varSpec,
      List<APIFilter> subsetFilters,
      ValueSpec valueSpec
  ) {
    // check variable compatibility with this functionality
    VariableDef var = metadata.getVariable(varSpec).orElseThrow();
    if (var.getSource() != VariableSource.NATIVE) {
      throw new IllegalArgumentException("Cannot call subsetting distribution endpoint with a non-native var: " + var);
    }
    if (var.getDataShape() != APIVariableDataShape.CATEGORICAL) {
      throw new IllegalArgumentException("Cannot call subsetting distribution endpoint with a non-categorical var (for now): " + var);
    }

    // build request object
    VariableDistributionPostRequest request = new VariableDistributionPostRequestImpl();
    request.setFilters(subsetFilters);
    request.setValueSpec(valueSpec);

    // build request url using internal endpoint (does not check user permissions via data access service)
    String url = getUrl("/ss-internal/studies/" + metadata.getStudyId() + "/entities/" + varSpec.getEntityId() + "/variables/" + varSpec.getVariableId() + "/distribution");

    // make request
    ResponseFuture response = ClientUtil.makeAsyncPostRequest(url, request, MediaType.APPLICATION_JSON, getAuthHeaderMap());

    // parse output and return
    try (InputStream responseBody = response.getInputStream()) {
      return JsonUtil.Jackson.readValue(responseBody, VariableDistributionPostResponse.class);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to complete subset distribution request.", e);
    }
  }

  // TODO move this raml type from the subset service to here?
  public Optional<VocabByRootEntityPostResponse> getVocabByRootEntity(
    ReferenceMetadata metadata,
    VariableSpec varSpec,
    List<APIFilter> subsetFilters
  ) {
    // check for annotations or throw
    // VariableDef var = metadata.getVariable(varSpec).orElseThrow();
    // if (!var.getHasStudyDependentVocabulary()) {
    //   throw new IllegalArgumentException("Cannot call subsetting vocabulary by root entity endpoint with a variable that does not have a study dependent vocabulary: " + var);
    // }
    // TODO should i check other things?

    // build request obj
    VocabByRootEntityPostRequest request = new VocabByRootEntityPostRequestImpl();
    request.setFilters(subsetFilters);

    // build request url
    // TODO understand the use of ss-internal vs not
    String url = getUrl("/studies/" + metadata.getStudyId() + "/entities" + varSpec.getEntityId() + "/variables/" + varSpec.getVariableId() + "/root-vocab");

    // make request
    ResponseFuture response = ClientUtil.makeAsyncPostRequest(url, request, MediaType.APPLICATION_JSON, getAuthHeaderMap());

    // parse output and return
    try (InputStream responseBody = response.getInputStream()) {
      return JsonUtil.Jackson.readValue(responseBody, VocabByRootEntityPostResponse.class);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to complete request for vocabulary by root entity.", e);
    } 
  }
}
