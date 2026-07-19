package org.veupathdb.service.eda.common.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.iterator.CloseableIterator;
import org.jetbrains.annotations.Nullable;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.common.client.spec.EdaSubsettingSpecValidator;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.client.spec.StreamSpecValidator;
import org.veupathdb.service.eda.common.model.MetaStudyResolver;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.StudyId;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.model.VariableSource;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.subset.model.Study;
import org.veupathdb.service.eda.subset.model.db.FilteredResultFactory;
import org.veupathdb.service.eda.subset.model.variable.VariableWithValues;
import org.veupathdb.service.eda.subset.model.variable.binary.BinaryFilesManager;
import org.veupathdb.service.eda.subset.service.ApiConversionUtil;
import org.veupathdb.service.eda.subset.service.RequestBundle;
import org.veupathdb.service.eda.subset.service.StudiesService;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EdaSubsettingClient implements StreamingDataClient {
  private static final Logger LOG = LogManager.getLogger(EdaSubsettingClient.class);

  /**
   * Returns the study detail for the study with the passed ID.
   *
   * @param studyId id of a study
   * @return study detail for the found study
   */
  public static APIStudyDetail getStudy(String studyId) {
    return ApiConversionUtil.getApiStudyDetail(Resources.getStudyResolver().getStudyById(studyId));
  }

  /**
   * Returns union-aware reference metadata for the passed study id token (plain or
   * {@code union(...)}). Single-study lookups are a pure pass-through (unchanged behavior). For a
   * union, the merged {@link APIStudyDetail} is served from {@link MetaStudyResolver}'s shared
   * cache, keyed by the union token plus each member's data-version (its {@code lastModified}),
   * so the Tier-1 merge is rebuilt only when the token is first seen or a member's data changes.
   *
   * @param studyId a plain study id or a union meta-study token
   * @return reference metadata for the found (or merged) study
   */
  public static ReferenceMetadata getReferenceMetadata(String studyId) {
    StudyId id = StudyId.parse(studyId);
    if (!id.isUnion()) {
      return new ReferenceMetadata(getStudy(id.getMembers().get(0)));
    }
    APIStudyDetail merged = MetaStudyResolver.resolveStudyDetailCached(
      id, EdaSubsettingClient::getStudy, EdaSubsettingClient::getMemberDataVersion);
    return new ReferenceMetadata(merged, id.getMembers());
  }

  /**
   * @return the data-version (epoch millis) of a study member, used to key the union meta-study
   * cache; {@code null} if the member's {@code lastModified} is not populated, in which case the
   * cache is bypassed for that union (see {@link MetaStudyResolver#resolveStudyDetailCached}).
   */
  private static Long getMemberDataVersion(String member) {
    java.util.Date lastModified = Resources.getStudyResolver().getStudyById(member).getLastModified();
    return lastModified == null ? null : lastModified.getTime();
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

    // Resolve schema based on whether study is user or curated.
    final String schemaName = StudiesService.resolveSchema(study);

    // convert study/streamspec to internal subsetting objects to check files vs DB and convert to internal classes
    final var variableNames = streamSpec.stream().map(VariableSpec::getVariableId).collect(Collectors.toList());
    final RequestBundle request = RequestBundle.unpack(schemaName, study, streamSpec.getEntityId(), variableFilters, variableNames, new APITabularReportConfigImpl());
    final BinaryFilesManager binaryFilesManager = Resources.getBinaryFilesManager();
    final boolean useFileBasedSubsetting = StudiesService.shouldRunFileBasedSubsetting(request, binaryFilesManager);

    LOG.debug("Resolved schema name: {} from study: {} with study source type: {}", schemaName, studyId, study.getStudySourceType());
    return FilteredResultFactory.tabularSubsetIterator(
      study,
      study.getEntity(streamSpec.getEntityId()).orElseThrow(),
      getVariablesFromStreamSpec(streamSpec, study),
      request.getFilters(),
      Resources.getBinaryValuesStreamer(),
      useFileBasedSubsetting,
      Resources.getApplicationDatabase(),
      schemaName
    );
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

  public static VariableDistributionPostResponse getCategoricalDistribution(
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

  public static InputStream getVocabByRootEntity(
    ReferenceMetadata metadata,
    VariableSpec varSpec,
    List<APIFilter> subsetFilters
  ) {
    var study = Resources.getStudyResolver().getStudyById(metadata.getStudyId());
    var schema = switch(study.getStudySourceType()) {
      case USER_SUBMITTED -> Resources.getVdiDatasetsSchema() + ".";
      case CURATED -> Resources.getAppDbSchema();
    };

    return StudiesService.getVocabByRootEntity(
      study,
      schema,
      varSpec.getEntityId(),
      varSpec.getVariableId(),
      ApiConversionUtil.toInternalFilters(study, subsetFilters, schema)
    );
  }

  @Nullable
  public static InputStream getVocabByRootEntity(
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
