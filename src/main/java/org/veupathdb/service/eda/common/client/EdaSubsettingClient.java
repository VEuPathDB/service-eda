package org.veupathdb.service.eda.common.client;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.client.ClientUtil;
import org.gusdb.fgputil.client.ResponseFuture;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.web.MimeTypes;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.common.client.spec.EdaSubsettingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.subset.model.Study;
import org.veupathdb.service.eda.subset.model.db.FilteredResultFactory;
import org.veupathdb.service.eda.subset.model.filter.Filter;
import org.veupathdb.service.eda.subset.model.variable.VariableWithValues;
import org.veupathdb.service.eda.subset.service.ApiConversionUtil;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.gusdb.fgputil.functional.Functions.swallowAndGet;

public class EdaSubsettingClient extends StreamingDataClient {
  private static final Logger LOG = LogManager.getLogger(EdaSubsettingClient.class);

  // request-scope cache for subsetting service metadata responses
  private List<String> _validStudyNameCache;

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
   * Returns the study detail for the study with the passed ID.
   *
   * @param studyId id of a study
   * @return study detail for the found study
   */
  public static APIStudyDetail getStudy(String studyId) {
    return ApiConversionUtil.getApiStudyDetail(Resources.getStudyResolver().getStudyById(studyId));
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
    StreamSpec spec
  ) throws ProcessingException {
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

  /**
   * Make a subsetting request without a network hop. This directly uses subsetting's FilteredResultFactory to
   * produce a stream of records that can be used by internal clients (i.e. the merging component).
   */
  public CloseableIterator<Map<String, String>> getTabularDataIterator(String studyId,
                                                                       List<APIFilter> variableFilters,
                                                                       StreamSpec streamSpec) {
    // Convert everything to internal subsetting classes to bridge the gap from merging/data services.
    final Study study = Resources.getStudyResolver().getStudyById(studyId);

    // Use metadata cache directly, which bypasses user studies, since user studies don't currently have files.
    final boolean fileBasedSubsetting = Resources.getMetadataCache().studyHasFiles(study.getStudyId());

    // Resolve schema based on whether study is user or curated.
    final String schemaName = resolveSchema(study);
    LOG.debug("Resolved schema name: {} from study: {} with study source type: {}", schemaName, studyId, study.getStudySourceType());
    final List<Filter> internalFilters = ApiConversionUtil.toInternalFilters(study, variableFilters, schemaName);

    return FilteredResultFactory.tabularSubsetIterator(
      study,
      study.getEntity(streamSpec.getEntityId()).orElseThrow(),
      getVariablesFromStreamSpec(streamSpec, study),
      internalFilters,
      Resources.getBinaryValuesStreamer(),
      fileBasedSubsetting,
      Resources.getApplicationDataSource(),
      schemaName
    );
  }

  private static String resolveSchema(Study study) {
    return switch(study.getStudySourceType()) {
      case USER_SUBMITTED -> Resources.getVdiDatasetsSchema() + ".";
      case CURATED -> Resources.getAppDbSchema();
    };
  }

  private static List<VariableWithValues<?>> getVariablesFromStreamSpec(StreamSpec spec, Study study) {
    return spec.stream()
      .map(varSpec -> study.getEntity(spec.getEntityId()).orElseThrow().getVariableOrThrow(varSpec.getVariableId()))
      .map(var -> (VariableWithValues<?>) var)
      .collect(Collectors.toList());
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

  public ResponseFuture getVocabByRootEntity(
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
    String url = getUrl("/studies/" + metadata.getStudyId() + "/entities/" + varSpec.getEntityId() + "/variables/" + varSpec.getVariableId() + "/root-vocab");

    // make request
    return ClientUtil.makeAsyncPostRequest(url, request, MimeTypes.TEXT_TABULAR, getAuthHeaderMap());
  }

  public ResponseFuture getVocabByRootEntity(
    ReferenceMetadata metadata,
    CollectionSpec collectionSpec,
    List<APIFilter> subsetFilters
  ) {
    // TODO
    return null;
  }

  public ResponseFuture getVocabByRootEntity(
    ReferenceMetadata metadata,
    DynamicDataSpec dataSpec,
    List<APIFilter> subsetFilters
  ) {
    if (dataSpec.isCollectionSpec()) {
      return getVocabByRootEntity(metadata, dataSpec.getCollectionSpec(), subsetFilters);
    } else if (dataSpec.isVariableSpec()) {
      return getVocabByRootEntity(metadata, dataSpec.getVariableSpec(), subsetFilters);
    } else {
      return null;
    }
  }

}
