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

  /**
   * Counts distinct entity instances for a union meta-study by opening the merged, deduped
   * tabular stream (see {@link UnionTabularStreams#open}) and counting emitted rows. The merged
   * stream already dedups by composite id key, so counting rows == counting distinct instances.
   * The passed {@link UnionTabularStreams.TabularStreamOpener} is injected so this is testable
   * without a live DB.
   */
  public static long countUnionEntity(
    ReferenceMetadata metadata,
    String entityId,
    List<APIFilter> filters,
    UnionTabularStreams.TabularStreamOpener opener
  ) {
    // no added variables; only id columns are needed to count distinct entity instances
    StreamSpec spec = new StreamSpec("count-" + java.util.UUID.randomUUID(), entityId);
    long n = 0;
    try (CloseableIterator<Map<String, String>> it = UnionTabularStreams.open(metadata, filters, spec, opener)) {
      while (it.hasNext()) { it.next(); n++; }
    } catch (Exception e) {
      throw new RuntimeException("Failed counting union entity " + entityId, e);
    }
    return n;
  }

  public static long getSubsetCount(ReferenceMetadata metadata, String entityId, List<APIFilter> subsetFilters) {
    if (metadata.getMemberStudyIds().isEmpty()) {
      return StudiesService.handleCountRequest(metadata.getStudyId(), entityId, subsetFilters).getCount();
    }
    return countUnionEntity(metadata, entityId, subsetFilters,
      (studyId, f, sp) -> getTabularDataIterator(studyId, f, sp));
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

    if (!metadata.getMemberStudyIds().isEmpty()) {
      Map<String, Long> tally = UnionDistribution.tally(metadata, varSpec, subsetFilters,
        (studyId, f, sp) -> getTabularDataIterator(studyId, f, sp));
      return buildUnionDistributionResponse(tally, valueSpec);
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

  /**
   * Assembles a {@link VariableDistributionPostResponse} for a union meta-study from the deduped
   * category tally produced by {@link UnionDistribution#tally}, in the same shape as
   * {@link StudiesService#handleDistributionRequest} returns for the single-study (categorical,
   * i.e. {@code DiscreteDistribution}) case: one {@link HistogramBin} per distinct category
   * (binStart == binEnd == binLabel == the category value; {@code value} is the count, or the
   * proportion of the total tallied count when {@code valueSpec} is {@code PROPORTION}), plus
   * {@link HistogramStats} populated from the tally (subsetSize, numVarValues,
   * numDistinctEntityRecords all equal the summed tally count since the tally has no way to
   * observe missing/null values that were filtered out upstream; numMissingCases is therefore
   * reported as 0 and subsetMin/Max/Mean are left unset as they don't apply to a categorical var).
   */
  private static VariableDistributionPostResponse buildUnionDistributionResponse(
    Map<String, Long> tally,
    ValueSpec valueSpec
  ) {
    long total = tally.values().stream().mapToLong(Long::longValue).sum();

    List<HistogramBin> bins = new java.util.ArrayList<>();
    for (Map.Entry<String, Long> entry : tally.entrySet()) {
      HistogramBin bin = new HistogramBinImpl();
      bin.setBinStart(entry.getKey());
      bin.setBinEnd(entry.getKey());
      bin.setBinLabel(entry.getKey());
      bin.setValue(valueSpec == ValueSpec.PROPORTION
        ? (total == 0 ? 0.0 : (double) entry.getValue() / total)
        : entry.getValue());
      bins.add(bin);
    }

    HistogramStats stats = new HistogramStatsImpl();
    stats.setSubsetSize(total);
    stats.setNumVarValues(total);
    stats.setNumDistinctValues((long) tally.size());
    stats.setNumDistinctEntityRecords(total);
    stats.setNumMissingCases(0L);

    VariableDistributionPostResponse response = new VariableDistributionPostResponseImpl();
    response.setHistogram(bins);
    response.setStatistics(stats);
    return response;
  }

  public static InputStream getVocabByRootEntity(
    ReferenceMetadata metadata,
    VariableSpec varSpec,
    List<APIFilter> subsetFilters
  ) {
    if (!metadata.getMemberStudyIds().isEmpty()) {
      // Tier-1 already merged member vocabularies onto this variable; return that union directly
      // rather than re-querying the DB per member.
      VariableDef var = metadata.getVariable(varSpec).orElseThrow();
      return buildUnionVocabStream(metadata.getStudyId(), var.getVocabulary());
    }

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

  /**
   * Mirrors the byte format produced by {@code ExtendedRootVocabHandler.queryStudyVocab} for the
   * single-study path: one line per row, each line being {@code <pk>\t<value>}, rows separated by
   * the platform line separator, no header/footer. For a union there is no single per-instance
   * root pk backing each vocabulary value (the vocabulary here is the Tier-1 merged union across
   * member studies, not a per-row query), so the union token itself (the merged study id) is used
   * as the pk column for every row.
   */
  private static InputStream buildUnionVocabStream(String unionStudyId, List<String> vocabulary) {
    if (vocabulary == null || vocabulary.isEmpty()) {
      return new java.io.ByteArrayInputStream(new byte[0]);
    }
    StringBuilder sb = new StringBuilder();
    String rowDelimiter = System.lineSeparator();
    for (String value : vocabulary) {
      sb.append(unionStudyId).append('\t').append(value).append(rowDelimiter);
    }
    return new java.io.ByteArrayInputStream(sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
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
