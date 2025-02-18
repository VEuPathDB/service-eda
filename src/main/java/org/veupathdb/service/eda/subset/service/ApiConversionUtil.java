package org.veupathdb.service.eda.subset.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.FormatUtil;
import org.gusdb.fgputil.SortDirection;
import org.gusdb.fgputil.functional.TreeNode;
import org.veupathdb.service.eda.common.client.DatasetAccessClient.StudyDatasetInfo;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.subset.Utils;
import org.veupathdb.service.eda.subset.model.Entity;
import org.veupathdb.service.eda.subset.model.Study;
import org.veupathdb.service.eda.subset.model.StudyOverview;
import org.veupathdb.service.eda.subset.model.distribution.BinSpecWithRange;
import org.veupathdb.service.eda.subset.model.distribution.BinUnits;
import org.veupathdb.service.eda.subset.model.distribution.DateDistributionConfig;
import org.veupathdb.service.eda.subset.model.distribution.NumberDistributionConfig;
import org.veupathdb.service.eda.subset.model.distribution.ValueSpec;
import org.veupathdb.service.eda.subset.model.filter.DateRangeFilter;
import org.veupathdb.service.eda.subset.model.filter.DateSetFilter;
import org.veupathdb.service.eda.subset.model.filter.Filter;
import org.veupathdb.service.eda.subset.model.filter.LongitudeRangeFilter;
import org.veupathdb.service.eda.subset.model.filter.MultiFilter;
import org.veupathdb.service.eda.subset.model.filter.MultiFilterSubFilter;
import org.veupathdb.service.eda.subset.model.filter.NumberRangeFilter;
import org.veupathdb.service.eda.subset.model.filter.NumberSetFilter;
import org.veupathdb.service.eda.subset.model.filter.StringSetFilter;
import org.veupathdb.service.eda.subset.model.tabular.TabularHeaderFormat;
import org.veupathdb.service.eda.subset.model.varcollection.DateVarCollection;
import org.veupathdb.service.eda.subset.model.varcollection.FloatingPointVarCollection;
import org.veupathdb.service.eda.subset.model.varcollection.IntegerVarCollection;
import org.veupathdb.service.eda.subset.model.varcollection.StringVarCollection;
import org.veupathdb.service.eda.subset.model.varcollection.VarCollection;
import org.veupathdb.service.eda.subset.model.variable.*;

import javax.annotation.Nullable;

public class ApiConversionUtil {
  private static final Logger LOG = LogManager.getLogger(ApiConversionUtil.class);

  // TODO: temporary hack until the deprecated java.util.Date usages are purged
  //       from lib-eda-subsetting.
  private static final ZoneOffset DEFAULT_ZONE_OFFSET = OffsetDateTime.now().getOffset();

  /** converts model study object to API study object */
  public static APIStudyDetail getApiStudyDetail(Study study) {
    APIEntity apiEntityTree = entityTreeToAPITree(study.getEntityTree());
    APIStudyDetail apiStudyDetail = new APIStudyDetailImpl();
    apiStudyDetail.setId(study.getStudyId());
    apiStudyDetail.setRootEntity(apiEntityTree);
    apiStudyDetail.setHasMap(study.hasGeographicData());
    apiStudyDetail.setIsUserStudy(study.getStudySourceType() == StudyOverview.StudySourceType.USER_SUBMITTED);
    return apiStudyDetail;
  }

  /** converts model entity object to API entity object */
  public static APIEntity entityTreeToAPITree(TreeNode<Entity> root) {
    return root.mapStructure((entity, mappedChildren) -> {
      APIEntity apiEntity = new APIEntityImpl();
      apiEntity.setDescription(entity.getDescription());
      apiEntity.setDisplayName(entity.getDisplayName());
      apiEntity.setDisplayNamePlural(entity.getDisplayNamePlural());
      apiEntity.setId(entity.getId());
      apiEntity.setIdColumnName(entity.getPKColName());
      apiEntity.setIsManyToOneWithParent(entity.isManyToOneWithParent());
      apiEntity.setChildren(mappedChildren);
      apiEntity.setVariables(entity.getVariables().stream()
        .map(ApiConversionUtil::variableToAPIVariable)
        .collect(Collectors.toList()));
      apiEntity.setCollections(entity.getCollections().stream()
        .map(ApiConversionUtil::collectionToAPICollection)
        .collect(Collectors.toList()));
      return apiEntity;
    });
  }

  private static APICollection collectionToAPICollection(VarCollection<?,?> col) {
    APICollection collection = switch(col.getType()) {
      case DATE -> getDateCollection((DateVarCollection)col);
      case INTEGER -> getIntegerCollection((IntegerVarCollection)col);
      case NUMBER -> getFloatCollection((FloatingPointVarCollection<?>)col);
      case STRING -> getStringCollection((StringVarCollection)col);
    };
    collection.setId(col.getId());
    collection.setDisplayName(col.getDisplayName());
    collection.setDataShape(toApiShape(col.getDataShape()));
    collection.setImputeZero(col.getImputeZero());
    collection.setDistinctValuesCount(col.getDistinctValuesCount());
    collection.setVocabulary(col.getVocabulary());
    collection.setMemberVariableIds(col.getMemberVariableIds());
    collection.setIsProportion(col.getIsProportion());
    collection.setIsCompositional(col.getIsCompositional());
    collection.setNormalizationMethod(col.getNormalizationMethod());
    collection.setMember(col.getMember());
    collection.setMemberPlural(col.getMemberPlural());
    return collection;
  }

  private static APIVariableDataShape toApiShape(VariableDataShape dataShape) {
    return APIVariableDataShape.valueOf(dataShape.name());
  }

  private static APICollection getDateCollection(DateVarCollection col) {
    APIDateCollection dateCol = new APIDateCollectionImpl();
    dateCol.setDistributionDefaults(getDateDistributionDefaults(col.getDistributionConfig()));
    return dateCol;
  }

  private static APICollection getIntegerCollection(IntegerVarCollection col) {
    APIIntegerCollection intCol = new APIIntegerCollectionImpl();
    intCol.setDistributionDefaults(getIntegerDistributionDefaults(col.getDistributionConfig()));
    intCol.setUnits(col.getUnits());
    return intCol;
  }

  private static APICollection getFloatCollection(FloatingPointVarCollection<?> col) {
    APINumberCollection floatCol = new APINumberCollectionImpl();
    floatCol.setDistributionDefaults(getFloatDistributionDefaults(col.getDistributionConfig()));
    floatCol.setUnits(col.getUnits());
    floatCol.setPrecision(col.getPrecision());
    return floatCol;
  }

  private static APICollection getStringCollection(StringVarCollection col) {
    APIStringCollection stringCol = new APIStringCollectionImpl();
    stringCol.setDisplayName(col.getDisplayName());
    stringCol.setId(col.getId());
    stringCol.setVocabulary(col.getVocabulary());
    stringCol.setMemberVariableIds(col.getMemberVariableIds());
    stringCol.setDistinctValuesCount(col.getDistinctValuesCount());
    return stringCol;
  }

  /** converts model variable object to API variable object */
  private static APIVariable variableToAPIVariable(Variable var) {
    APIVariable apiVar = var.hasValues()
      ? getValuesVar((VariableWithValues<?>)var)
      : new APIVariablesCategoryImpl();
    // set props common to all variables
    apiVar.setId(var.getId());
    apiVar.setDisplayName(var.getDisplayName());
    apiVar.setDisplayType(APIVariableDisplayType.valueOf(var.getDisplayType().name()));
    apiVar.setProviderLabel(var.getProviderLabel());
    apiVar.setParentId(var.getParentId());
    apiVar.setDefinition(var.getDefinition());
    apiVar.setDisplayOrder(var.getDisplayOrder());
    apiVar.setHideFrom(var.getHideFrom() == null ? Collections.emptyList() : var.getHideFrom());
    return apiVar;
  }

  /** instantiates API values var and sets values-var-specific props */
  private static APIVariableWithValues getValuesVar(VariableWithValues<?> var) {
    APIVariableWithValues apiVar = switch(var.getType()) {
      case DATE -> getDateVar((DateVariable)var);
      case INTEGER -> getIntegerVar((IntegerVariable)var);
      case NUMBER -> getFloatVar((FloatingPointVariable)var);
      case LONGITUDE -> getLongitudeVar((LongitudeVariable)var);
      case STRING -> getStringVar((StringVariable)var);
    };
    // set props common to all variables with values
    apiVar.setDataShape(toApiShape(var.getDataShape()));
    apiVar.setVocabulary(var.getVocabulary());
    apiVar.setDistinctValuesCount(var.getDistinctValuesCount());
    apiVar.setIsFeatured(var.getIsFeatured());
    apiVar.setIsMergeKey(var.getIsMergeKey());
    apiVar.setIsMultiValued(var.getIsMultiValued());
    apiVar.setIsTemporal(var.getIsTemporal());
    apiVar.setImputeZero(var.getImputeZero());
    apiVar.setHasStudyDependentVocabulary(var.hasStudyDependentVocabulary());
    apiVar.setVariableSpecToImputeZeroesFor(VariableDef.newVariableSpecFromDotNotation(
      var.getVariableSpecToImputeZeroesFor()));
    return apiVar;
  }

  /** instantiates API date var and sets date-specific props */
  private static APIDateVariable getDateVar(DateVariable var) {
    APIDateVariable apiVar = new APIDateVariableImpl();
    apiVar.setDistributionDefaults(getDateDistributionDefaults(var.getDistributionConfig()));
    return apiVar;
  }

  private static APIDateDistributionDefaults getDateDistributionDefaults(DateDistributionConfig distributionConfig) {
    APIDateDistributionDefaults defaults = new APIDateDistributionDefaultsImpl();
    defaults.setRangeMin(distributionConfig.rangeMin);
    defaults.setRangeMax(distributionConfig.rangeMax);
    defaults.setDisplayRangeMin(distributionConfig.displayRangeMin);
    defaults.setDisplayRangeMax(distributionConfig.displayRangeMax);
    defaults.setBinWidth(distributionConfig.binSize);
    defaults.setBinWidthOverride(null);
    defaults.setBinUnits(toApiBinUnits(distributionConfig.binUnits));
    return defaults;
  }

  private static APIIntegerVariable getIntegerVar(IntegerVariable var) {
    APIIntegerVariable apiVar = new APIIntegerVariableImpl();
    apiVar.setDistributionDefaults(getIntegerDistributionDefaults(var.getDistributionConfig()));
    apiVar.setUnits(var.getUnits());
    return apiVar;
  }

  private static APIIntegerDistributionDefaults getIntegerDistributionDefaults(NumberDistributionConfig<Long> distributionConfig) {
    APIIntegerDistributionDefaults defaults = new APIIntegerDistributionDefaultsImpl();
    defaults.setBinWidth(distributionConfig.getBinWidth());
    defaults.setBinWidthOverride(distributionConfig.getBinWidthOverride());
    defaults.setDisplayRangeMin(distributionConfig.getDisplayRangeMin());
    defaults.setDisplayRangeMax(distributionConfig.getDisplayRangeMax());
    defaults.setRangeMin(distributionConfig.getRangeMin());
    defaults.setRangeMax(distributionConfig.getRangeMax());
    return defaults;
  }

  private static APINumberVariable getFloatVar(FloatingPointVariable var) {
    APINumberVariable apiVar = new APINumberVariableImpl();
    apiVar.setDistributionDefaults(getFloatDistributionDefaults(var.getDistributionConfig()));
    apiVar.setUnits(var.getUnits());
    apiVar.setPrecision(var.getPrecision());
    apiVar.setScale(toApiScale(var.getScale()));
    return apiVar;
  }

  private static APIVariableScale toApiScale(VariableScale scale) {
    return scale == null ? null : switch(scale) {
      case LOG_10 -> APIVariableScale.LOG;
      case LOG_2 -> APIVariableScale.LOG2;
      case NATURAL_LOG -> APIVariableScale.LN;
    };
  }

  private static APINumberDistributionDefaults getFloatDistributionDefaults(NumberDistributionConfig<Double> distributionConfig) {
    APINumberDistributionDefaults defaults = new APINumberDistributionDefaultsImpl();
    defaults.setBinWidth(distributionConfig.getBinWidth());
    defaults.setBinWidthOverride(distributionConfig.getBinWidthOverride());
    defaults.setDisplayRangeMin(distributionConfig.getDisplayRangeMin());
    defaults.setDisplayRangeMax(distributionConfig.getDisplayRangeMax());
    defaults.setRangeMin(distributionConfig.getRangeMin());
    defaults.setRangeMax(distributionConfig.getRangeMax());
    return defaults;
  }

  private static APIStringVariable getStringVar(StringVariable var) {
    // currently no string-specific extra properties
    return new APIStringVariableImpl();
  }

  private static APILongitudeVariable getLongitudeVar(LongitudeVariable var) {
    APILongitudeVariable apiVar = new APILongitudeVariableImpl();
    apiVar.setPrecision(var.getPrecision());
    return apiVar;
  }

  public static List<HistogramBin> toApiHistogramBins(List<org.gusdb.fgputil.distribution.HistogramBin> histogramData) {
    return histogramData.stream().map(modelBin -> {
      HistogramBin bin = new HistogramBinImpl();
      bin.setBinStart(modelBin.getBinStart());
      bin.setBinEnd(modelBin.getBinEnd());
      bin.setValue(modelBin.getValue());
      bin.setBinLabel(modelBin.getBinLabel());
      return bin;
    }).collect(Collectors.toList());
  }

  public static HistogramStats toApiHistogramStats(org.gusdb.fgputil.distribution.HistogramStats statistics) {
    HistogramStats stats = new HistogramStatsImpl();
    stats.setSubsetMin(statistics.getSubsetMin());
    stats.setSubsetMax(statistics.getSubsetMax());
    stats.setSubsetMean(statistics.getSubsetMean());
    stats.setSubsetSize(statistics.getSubsetSize());
    stats.setNumVarValues(statistics.getNumVarValues());
    stats.setNumDistinctValues(statistics.getNumDistinctValues());
    stats.setNumDistinctEntityRecords(statistics.getNumDistinctEntityRecords());
    stats.setNumMissingCases(statistics.getNumMissingCases());
    return stats;
  }

  public static Optional<BinSpecWithRange> toInternalBinSpecWithRange(org.veupathdb.service.eda.generated.model.BinSpecWithRange binSpec) {
    return Optional.ofNullable(binSpec)
      .map(b -> new BinSpecWithRange()
        .setDisplayRangeMin(b.getDisplayRangeMin())
        .setDisplayRangeMax(b.getDisplayRangeMax())
        .setBinWidth(b.getBinWidth())
        .setBinUnits(toInternalBinUnits(b.getBinUnits())));
  }

  private static BinUnits toInternalBinUnits(org.veupathdb.service.eda.generated.model.BinUnits binUnits) {
    return binUnits == null ? null : BinUnits.valueOf(binUnits.name());
  }

  private static org.veupathdb.service.eda.generated.model.BinUnits toApiBinUnits(BinUnits binUnits) {
    return binUnits == null ? null : org.veupathdb.service.eda.generated.model.BinUnits.valueOf(binUnits.name());
  }

  public static ValueSpec toInternalValueSpec(org.veupathdb.service.eda.generated.model.ValueSpec valueSpec) {
    return valueSpec == null ? null : ValueSpec.valueOf(valueSpec.name());
  }

  public static List<org.veupathdb.service.eda.subset.model.tabular.SortSpecEntry> toInternalSorting(List<SortSpecEntry> sorting) {
    return sorting == null ? null : sorting.stream()
      .map(e -> new org.veupathdb.service.eda.subset.model.tabular.SortSpecEntry()
        .setDirection(SortDirection.valueOf(e.getDirection().name()))
        .setKey(e.getKey()))
      .collect(Collectors.toList());
  }

  public static TabularHeaderFormat toInternalTabularHeaderFormat(org.veupathdb.service.eda.generated.model.TabularHeaderFormat headerFormat) {
    return headerFormat == null ? null : TabularHeaderFormat.valueOf(headerFormat.name());
  }

  public static org.veupathdb.service.eda.subset.model.tabular.DataSourceType toInternalDataSourceType(DataSourceType dataSourceType) {
    return org.veupathdb.service.eda.subset.model.tabular.DataSourceType.fromValue(dataSourceType.getValue());
  }

  public static List<APIStudyOverview> toApiStudyOverviews(
    Map<String, StudyDatasetInfo> datasetInfoMap,
    Map<String, StudyOverview> overviewMap) {
    return datasetInfoMap.keySet().stream()
      .filter(studyId -> {
        final boolean studyInOverviews = overviewMap.containsKey(studyId);
        if (!studyInOverviews) {
          LOG.warn("Found study {} in visible studies but not it's not being returned from canonical list of studies.", studyId);
        }
        return studyInOverviews;
      })
      .map(studyId -> {
        StudyDatasetInfo dataset = datasetInfoMap.get(studyId);
        StudyOverview overview = overviewMap.get(studyId);
        APIStudyOverview study = new APIStudyOverviewImpl();
        study.setId(studyId);
        study.setSourceType(switch (overview.getStudySourceType()) {
          case CURATED -> StudySourceType.CURATED;
          case USER_SUBMITTED -> StudySourceType.USERSUBMITTED;
        });
        study.setDatasetId(dataset.getDatasetId());
        study.setSha1hash(dataset.getSha1Hash());
        study.setDisplayName(dataset.getDisplayName());
        study.setShortDisplayName(dataset.getShortDisplayName());
        study.setDescription(dataset.getDescription());
        study.setLastModified(dateToOffsetDate(overview.getLastModified()));
        return study;
      }).toList();
  }

  /*
   * Given a study and a set of API filters, construct and return a set of filters, each being the appropriate
   * filter subclass
   */
  public static List<Filter> toInternalFilters(Study study, List<APIFilter> filters, String appDbSchema) {
    List<Filter> subsetFilters = new ArrayList<>();

    // FIXME: need this until we turn on schema-level checking to enforce requiredness
    if (filters == null) return subsetFilters;

    String errPrfx = "A filter references an unfound ";

    for (APIFilter apiFilter : filters) {

      // validate filter's entity id
      Supplier<BadRequestException> excep =
        () -> new BadRequestException(errPrfx + "entity ID: " + apiFilter.getEntityId());
      Entity entity = study.getEntity(apiFilter.getEntityId()).orElseThrow(excep);

      Filter newFilter = switch (apiFilter) {
        case APIDateRangeFilter ignored -> unpackDateRangeFilter(apiFilter, entity, appDbSchema);
        case APIDateSetFilter ignored -> unpackDateSetFilter(apiFilter, entity, appDbSchema);
        case APINumberRangeFilter ignored -> unpackNumberRangeFilter(apiFilter, entity, appDbSchema);
        case APINumberSetFilter ignored -> unpackNumberSetFilter(apiFilter, entity, appDbSchema);
        case APILongitudeRangeFilter ignored -> unpackLongitudeRangeFilter(apiFilter, entity, appDbSchema);
        case APIStringSetFilter ignored -> unpackStringSetFilter(apiFilter, entity, appDbSchema);
        case APIMultiFilter ignored -> unpackMultiFilter(apiFilter, entity, appDbSchema);
        default -> throw new InternalServerErrorException("Input filter not an expected subclass of Filter");
      };

      subsetFilters.add(newFilter);
    }
    return subsetFilters;
  }

  private static DateRangeFilter unpackDateRangeFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APIDateRangeFilter f = (APIDateRangeFilter)apiFilter;
    if (f.getMin() == null) throw new BadRequestException("Date range filter: min is a required property");
    if (f.getMax() == null) throw new BadRequestException("Date range filter: max is a required property");
    DateVariable var = DateVariable.assertType(entity.getVariableOrThrow(f.getVariableId()));
    return new DateRangeFilter(appDbSchema, entity, var,
      FormatUtil.parseDateTime(Utils.standardizeLocalDateTime(f.getMin())),
      FormatUtil.parseDateTime(Utils.standardizeLocalDateTime(f.getMax())));
  }

  private static DateSetFilter unpackDateSetFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APIDateSetFilter f = (APIDateSetFilter)apiFilter;
    if (f.getDateSet() == null || f.getDateSet().isEmpty())
      throw new BadRequestException(("Date set filter: >0 dates must be specified"));
    DateVariable var = DateVariable.assertType(entity.getVariableOrThrow(f.getVariableId()));
    List<LocalDateTime> dateSet = new ArrayList<>();
    for (String dateStr : f.getDateSet()) {
      dateSet.add(FormatUtil.parseDateTime(Utils.standardizeLocalDateTime(dateStr)));
    }
    return new DateSetFilter(appDbSchema, entity, var, dateSet);
  }

  private static NumberRangeFilter<?> unpackNumberRangeFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APINumberRangeFilter f = (APINumberRangeFilter)apiFilter;
    if (f.getMin() == null) throw new BadRequestException("Number range filter: min is a required property");
    if (f.getMax() == null) throw new BadRequestException("Number range filter: max is a required property");

    Variable var = entity.getVariableOrThrow(f.getVariableId());

    // need to check for each number variable type
    Optional<NumberRangeFilter<Long>> intFilter = IntegerVariable.assertType(var)
      .map(intVar -> new NumberRangeFilter<>(appDbSchema, entity, intVar, f.getMin(), f.getMax()));
    if (intFilter.isPresent()) return intFilter.get();

    // not integer var; try floating point or else throw
    return FloatingPointVariable.assertType(var)
      .map(floatVar -> new NumberRangeFilter<>(appDbSchema, entity, floatVar, f.getMin(), f.getMax()))
      .orElseThrow(() -> new BadRequestException("Variable " + var.getId() +
        " of entity " + var.getEntityId() + " is not a number or integer variable."));
  }

  private static NumberSetFilter<?> unpackNumberSetFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APINumberSetFilter f = (APINumberSetFilter)apiFilter;
    if (f.getNumberSet() == null || f.getNumberSet().isEmpty())
      throw new BadRequestException(("Number set filter: >0 numbers must be specified"));

    Variable var = entity.getVariableOrThrow(f.getVariableId());

    // need to check for each number variable type
    Optional<NumberSetFilter<Long>> intFilter = IntegerVariable.assertType(var)
      .map(intVar -> new NumberSetFilter<>(appDbSchema, entity, intVar, f.getNumberSet()));
    if (intFilter.isPresent()) return intFilter.get();

    // not integer var; try floating point or else throw
    return FloatingPointVariable.assertType(var)
      .map(floatVar -> new NumberSetFilter<>(appDbSchema, entity, floatVar, f.getNumberSet()))
      .orElseThrow(() -> new BadRequestException("Variable " + var.getId() +
        " of entity " + var.getEntityId() + " is not a number or integer variable."));
  }

  private static LongitudeRangeFilter unpackLongitudeRangeFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APILongitudeRangeFilter f = (APILongitudeRangeFilter)apiFilter;
    if (f.getLeft() == null) throw new BadRequestException("Longitude range filter: left is a required property");
    if (f.getRight() == null) throw new BadRequestException("Longitude range filter: right is a required property");
    LongitudeVariable var = LongitudeVariable.assertType(entity.getVariableOrThrow(f.getVariableId()));
    return new LongitudeRangeFilter(appDbSchema, entity, var, f.getLeft(), f.getRight());
  }

  private static StringSetFilter unpackStringSetFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APIStringSetFilter f = (APIStringSetFilter)apiFilter;
    if (f.getStringSet() == null || f.getStringSet().isEmpty())
      throw new BadRequestException(("String set filter: >0 strings must be specified"));
    StringVariable stringVar = StringVariable.assertType(entity.getVariableOrThrow(f.getVariableId()));
    return new StringSetFilter(appDbSchema, entity, stringVar, f.getStringSet());
  }

  private static MultiFilter unpackMultiFilter(APIFilter apiFilter, Entity entity, String appDbSchema) {
    APIMultiFilter f = (APIMultiFilter)apiFilter;
    if (f.getSubFilters().isEmpty())
      throw new BadRequestException("Multifilter may not have an empty list of subFilters");

    // validate multifilter variable
    String mfVarId = f.getVariableId();
    Variable multiFilterVariable = entity.getVariable(mfVarId).orElseThrow(() ->
      new BadRequestException("Multifilter includes invalid multifilter variable ID: " + mfVarId));
    if (multiFilterVariable.getDisplayType() != VariableDisplayType.MULTIFILTER)
      throw new BadRequestException("Multifilter variable does not have display type 'multifilter': " + mfVarId);

    // validate subfilters
    List<MultiFilterSubFilter> subFilters = new ArrayList<>();
    for (APIMultiFilterSubFilter apiSubFilter : f.getSubFilters()) {
      String variableId = apiSubFilter.getVariableId();
      if (!entity.getMultiFilterMap().get(mfVarId).contains(variableId))
        throw new BadRequestException("Multifilter includes subfilter with invalid variable: " + variableId);
      Variable var = entity.getVariable(variableId).orElseThrow(() -> new BadRequestException("Multifilter includes invalid variable ID: " + variableId));
      StringVariable subFilterVar = StringVariable.assertType(var);
      subFilters.add(new MultiFilterSubFilter(subFilterVar, apiSubFilter.getStringSet()));
    }

    return new MultiFilter(appDbSchema, entity, subFilters, MultiFilter.MultiFilterOperation.fromString(f.getOperation().getValue()));
  }

  @Nullable
  private static OffsetDateTime dateToOffsetDate(@Nullable Date date) {
    return date == null ? null : Instant.ofEpochMilli(date.getTime()).atOffset(DEFAULT_ZONE_OFFSET);
  }
}
