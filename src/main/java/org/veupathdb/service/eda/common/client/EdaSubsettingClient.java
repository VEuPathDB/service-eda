package org.veupathdb.service.eda.common.client;

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
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.subset.model.Study;
import org.veupathdb.service.eda.subset.model.db.FilteredResultFactory;
import org.veupathdb.service.eda.subset.model.filter.Filter;
import org.veupathdb.service.eda.subset.model.variable.VariableWithValues;
import org.veupathdb.service.eda.subset.service.ApiConversionUtil;
import org.veupathdb.service.eda.subset.service.StudiesService;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class EdaSubsettingClient extends ServiceClient implements StreamingDataClient {
  private static final Logger LOG = LogManager.getLogger(EdaSubsettingClient.class);

  // request-scope cache for subsetting service metadata responses
  private List<String> _validStudyNameCache;

  public EdaSubsettingClient(String serviceBaseUrl, Entry<String, String> authHeader) {
    super(serviceBaseUrl, authHeader);
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

  /**
   * Make a subsetting request without a network hop.
   * <p>
   * This directly uses subsetting's FilteredResultFactory to produce a stream
   * of records that can be used by internal clients (i.e. the merging
   * component).
   */
  public static CloseableIterator<Map<String, String>> getTabularDataIterator(
    String studyId,
    List<APIFilter> variableFilters,
    StreamSpec streamSpec
  ) {
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

  public static long getSubsetCount(ReferenceMetadata metadata, String entityId, List<APIFilter> subsetFilters) {
    return StudiesService.handleCountRequest(metadata.getStudyId(), entityId, subsetFilters).getCount();
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

    return StudiesService.handleDistributionRequest(
      metadata.getStudyId(),
      varSpec.getEntityId(),
      varSpec.getVariableId(),
      request
    );
  }

  public ResponseFuture getVocabByRootEntity(
    ReferenceMetadata metadata,
    VariableSpec varSpec,
    List<APIFilter> subsetFilters
  ) {
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
    DynamicDataSpec dataSpec,
    List<APIFilter> subsetFilters
  ) {
    if (dataSpec.isCollectionSpec()) {
      // TODO
      return null;
    } else if (dataSpec.isVariableSpec()) {
      return getVocabByRootEntity(metadata, dataSpec.getVariableSpec(), subsetFilters);
    } else {
      return null;
    }
  }

}
