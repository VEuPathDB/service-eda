package org.veupathdb.service.eda.data.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vulpine.lib.jcfi.CheckedFunction;
import jakarta.ws.rs.BadRequestException;
import kotlin.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.Timer;
import org.gusdb.fgputil.Tuples.ThreeTuple;
import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.FunctionalInterfaces.ConsumerWithException;
import org.gusdb.fgputil.functional.Functions;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.jetbrains.annotations.Nullable;
import org.veupathdb.service.eda.common.client.*;
import org.veupathdb.service.eda.common.client.EdaComputeClient.ComputeRequestBody;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.model.CollectionDef;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.common.plugin.constraint.DataElementSet;
import org.veupathdb.service.eda.common.plugin.constraint.DataElementValidator;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.generated.model.BinSpec.RangeType;
import org.veupathdb.service.eda.merge.ServiceExternal;
import org.veupathdb.service.eda.merge.core.request.ComputeInfo;
import org.veupathdb.service.eda.util.Exceptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.singleQuote;

/**
 * Base visualization plugin for all other plugins.  Provides access to parts of
 * the request object, manages logic flow over the course of the request, and
 * provides streaming merged data to subclasses for processing per specs provided
 * by those subclasses.
 *
 * @param <T> type of request (must extend DataPluginRequestBase)
 * @param <S> plugin's spec class (must be or extend the generated spec class for this plugin)
 * @param <R> plugin's compute spec class (must be or extend the generated compute spec class for this plugin)
 */
public abstract class AbstractPlugin<T extends DataPluginRequestBase, S, R> {

  private static final Logger LOG = LogManager.getLogger(AbstractPlugin.class);

  // shared stream name for plugins that need request only a single stream
  protected static final String DEFAULT_SINGLE_STREAM_NAME = "single_tabular_dataset";
  // shared stream name for plugins that need request multiple streams
  protected static final String ADDITIONAL_STREAM_NAME = "second_tabular_dataset";

  protected class ClassGroup extends ThreeTuple<Class<T>,Class<S>,Class<R>> {
    public ClassGroup(Class<T> visualizationRequestClass, Class<S> visualizationSpecClass, Class<R> computeConfigClass) {
      super(visualizationRequestClass, visualizationSpecClass, computeConfigClass);
    }
    public Class<T> getVisualizationRequestClass() { return getFirst(); }
    public Class<S> getVisualizationSpecClass() { return getSecond(); }
    public Class<R> getComputeConfigClass() { return getThird(); }
  }

  // methods that need to be implemented
  protected abstract ClassGroup getTypeParameterClasses();
  protected abstract void validateVisualizationSpec(S pluginSpec) throws ValidationException;
  protected abstract List<StreamSpec> getRequestedStreams(S pluginSpec);
  protected abstract void writeResults(OutputStream out, Map<String, InputStream> dataStreams) throws IOException;

  // methods that should probably be overridden
  public String getDisplayName() { return getClass().getName(); }
  public String getDescription() { return ""; }
  public List<String> getProjects() { return Collections.emptyList(); }
  // have to decide if default is 1 and 25 override or vice versa. to facet or not, that is the question...
  public Integer getMaxPanels() { return 1; }
  public ConstraintSpec getConstraintSpec() { return new ConstraintSpec(); }

  protected ReferenceMetadata _referenceMetadata;
  // stored compute name and typed value of the passed compute config object (if plugin requires compute)
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  protected Optional<TwoTuple<String, R>> _computeInfo;

  protected S _pluginSpec;
  protected List<StreamSpec> _requiredStreams;

  private Timer _timer;
  private boolean _requestProcessed = false;
  private String _studyId;
  private List<APIFilter> _subsetFilters;
  private List<DerivedVariableSpec> _derivedVariableSpecs;

  /**
   * Processes the plugin request and prepares this plugin to receive an OutputStream via the
   * accept() method, to which it will write its response.  As much processing as possible should
   * be done in this method up front, since once accept() is called, we are tied to a 2xx response.
   *
   * @param appName app name this plugin belongs to; can be null if no compute is expected/allowed
   * @param request incoming plugin request object
   * @return a consumer for an output stream that will write the response
   * @throws ValidationException if request validation fails
   */
  public final Consumer<OutputStream> processRequest(String appName, T request) throws ValidationException {

    // start request timer (used to profile request performance dynamics)
    _timer = new Timer();
    logRequestTime("Starting timer");

    // validate config types match classes provided by subclass
    _pluginSpec = getSpecObject(request, "getConfig", getTypeParameterClasses().getVisualizationSpecClass());

    // find compute name if required by this viz plugin; if present, then look up compute config
    //   and create an optional tuple of name+config (empty optional if viz does not require compute)
    _computeInfo = appName == null
      ? Optional.empty()
      : findComputeName(appName).map(name -> new TwoTuple<>(
        name,
        getSpecObject(request, "getComputeConfig", getTypeParameterClasses().getComputeConfigClass())
      ));

    // check for subset and derived entity properties of request
    _subsetFilters = Optional.ofNullable(request.getFilters()).orElse(Collections.emptyList());
    _derivedVariableSpecs = Optional.ofNullable(request.getDerivedVariables()).orElse(Collections.emptyList());

    // get study
    APIStudyDetail study = EdaSubsettingClient.getStudy(request.getStudyId());
    _studyId = study.getId();

    // if plugin requires a compute, check if compute results are available
    if (_computeInfo.isPresent() && !isComputeResultsAvailable()) {
      throw new BadRequestException("Compute results are not available for the requested job.");
    }

    // construct available variables for each entity from metadata and derived variable config
    _referenceMetadata = new ReferenceMetadata(study);

    // if derived vars present, get derived var metadata and incorporate
    Exceptions.errToBadRequest(() -> {
      ServiceExternal.processDvMetadataRequest(_studyId, _derivedVariableSpecs)
        .forEach(_referenceMetadata::incorporateDerivedVariable);
    });

    // if plugin requires a compute, get computed var metadata and incorporate
    if (_computeInfo.isPresent() && computeGeneratesVars())
      _referenceMetadata.incorporateComputedVariables(getComputedVariableMetadata().getVariables());

    // ask subclass to validate the configuration
    validateVisualizationSpec(_pluginSpec);

    // get list of data streams required by this subclass
    _requiredStreams = getRequestedStreams(_pluginSpec);

    // validate stream specs provided by the subclass
    new EdaMergingClient().getStreamSpecValidator()
      .validateStreamSpecs(_requiredStreams, _referenceMetadata).throwIfInvalid();

    _requestProcessed = true;
    logRequestTime("Initial request processing complete");

    CheckedFunction<StreamSpec, InputStream> streamGenerator = spec -> EdaMergingClient.getTabularDataStream(
      _referenceMetadata,
      _subsetFilters,
      _derivedVariableSpecs,
      _computeInfo.map(info -> new ComputeInfo(
        info.getFirst(),
        new EdaComputeClient.ComputeRequestBody(
          request.getStudyId(),
          request.getFilters(),
          request.getDerivedVariables(),
          info.getSecond()
        )
      )).orElse(null),
      spec
    );

    @SuppressWarnings("resource") // closed by StreamingDataClient.processDataStreams
    final var dataStreams = StreamingDataClient.buildDataStreams(_requiredStreams, streamGenerator);

    return out -> {
      if (!_requestProcessed) {
        throw new RuntimeException("Output cannot be streamed until request has been processed.");
      }

      // create stream processor
      // TODO: might make disallowing empty results optional in the future; this is the original implementation
      //ConsumerWithException<Map<String,InputStream>> streamProcessor = map -> writeResults(out, map);
      ConsumerWithException<Map<String, InputStream>> streamProcessor = map -> writeResults(out,
        Functions.mapValues(map, entry -> new NonEmptyResultStream(entry.getKey(), entry.getValue())));

      // build and process streams
      logRequestTime("Making requests for data streams");
      LOG.info("Building and processing {} required data streams.", _requiredStreams.size());
      StreamingDataClient.processDataStreams(_requiredStreams, dataStreams, streamProcessor);
      logRequestTime("Data streams processed; response written; request complete");
    };
  }

  @Nullable
  protected ComputeInfo makeComputeInfo() {
    return null;
  }

  /**
   * @return whether the associated compute generates vars (assumes true unless overridden)
   */
  protected boolean computeGeneratesVars() {
    return true;
  }

  protected void logRequestTime(String eventDescription) {
    LOG.info("Request Time: {}ms, {}", _timer.getElapsed(), eventDescription);
  }

  protected S getPluginSpec() {
    return _pluginSpec;
  }

  protected List<APIFilter> getSubsetFilters() {
    return _subsetFilters;
  }

  protected ReferenceMetadata getReferenceMetadata() {
    return _referenceMetadata;
  }

  protected PluginUtil getUtil() {
    return new PluginUtil(getReferenceMetadata(), EdaMergingClient::columnHeaderFor);
  }

  protected void validateInputs(DataElementSet values) {
    new DataElementValidator(getReferenceMetadata(), getConstraintSpec()).validate(values);
  }

  @SuppressWarnings("unchecked")
  private <Q> Q getSpecObject(T request, String methodName, Class<Q> specClass) {
    try {
      Method configGetter = request.getClass().getMethod(methodName);
      Object config = configGetter.invoke(request);
      if (specClass.isAssignableFrom(config.getClass())) {
        return (Q)config;
      }
      throw new RuntimeException("Plugin class " + getClass().getName() +
        " declares spec class "  + specClass.getName() +
        " but " + request.getClass().getName() + "::" + methodName + "()" +
        " returned " + config.getClass().getName() + ". The second must be a subclass of the first.");
    }
    catch (NoSuchMethodException noSuchMethodException) {
      throw new RuntimeException("Generated class " + request.getClass().getName() +
        " must implement a no-arg method " + methodName + "() which returns an instance of " + specClass.getName());
    }
    catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Misconfiguration of visualization plugin: " + getClass().getName(), e);
    }
  }

  // Same description as method below but uses filters assigned to this plugin (i.e. non-custom)
  protected <U extends DataPluginRequestBase, V, W> W invokePlugin(
    AbstractEmptyComputePlugin<U,V> plugin, V visualizationConfig, Class<W> expectedResponseType) {
    return invokePlugin(plugin, visualizationConfig, expectedResponseType, _subsetFilters);
  }

  /**
   * Enables visualization plugins to call each other to get any required data.
   * <p>
   * The same study, subset filters, and derived variable specs passed to this
   * instance will be used to call the other plugin.  This process has a number
   * of caveats:
   * <p>
   * <ol>
   *   <li>The call is synchronous, so the calling plugin blocks until the
   *       result is collected</li>
   *   <li>The result is put into a response object for that plugin and stored
   *       in memory, so should not be large</li>
   *   <li>The plugin to be called cannot have a compute.  In the future, we
   *       could maybe have it depend on the same compute (and compute config)
   *       as the calling plugin, but beyond that this plugin instance does not
   *       have enough information to make that call.</li>
   *   <li>Even though the caller and callee plugins must use the same study,
   *       the callee will still refetch the ReferenceMetadata from subsetting.
   *       Maybe we can pass it in somehow to allow the callee to skip this
   *       expensive step.</li>
   * </ol>
   *
   * @param plugin plugin to call
   *
   * @param visualizationConfig configuration object specific to this plugin
   * (i.e. the spec, normally attached to the config JSON property).
   *
   * @param expectedResponseType expected response type of the plugin to be
   * called.  This can be either the interface or implementation class of the
   * response type
   *
   * @param subsetFilters filters to send to the plugin to retrieve data streams
   * used to generate plugin response
   *
   * @param <U> type of request body for the plugin
   *
   * @param <V> type of the config object for the plugin (set on the undeclared
   * config property of U)
   *
   * @param <W> type of response generated by the plugin to be called
   *
   * @return response generated by the called plugin
   */
  @SuppressWarnings("unchecked")
  protected <U extends DataPluginRequestBase, V, W> W invokePlugin(
    AbstractEmptyComputePlugin<U,V> plugin,
    V visualizationConfig,
    Class<W> expectedResponseType,
    List<APIFilter> subsetFilters
  ) {
    try {
      // build plugin request
      String requestClassName = plugin.getTypeParameterClasses().getVisualizationRequestClass().getName();
      // need to use the implementation class (not the interface) so we can instantiate it
      if (!requestClassName.endsWith("Impl")) requestClassName += "Impl";
      U request = (U)Class.forName(requestClassName).getConstructor().newInstance();
      request.setStudyId(_studyId);
      request.setFilters(subsetFilters);
      request.setDerivedVariables(_derivedVariableSpecs); // probably not needed; how are these relevant?
      // config is not a property on DataPluginRequestBase, but we always expect it; invoke with the passed config
      plugin.getTypeParameterClasses().getVisualizationRequestClass().getMethod("setConfig",
        plugin.getTypeParameterClasses().getVisualizationSpecClass()).invoke(request, visualizationConfig);
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      // passing null as appName because it is only used to look up the compute; since this is an EmptyComputePlugin, we know there is no compute
      plugin.processRequest(null, request).accept(result);
      // need to use the implementation class (not the interface) so Jackson can instantiate it
      String expectedResponseTypeName = expectedResponseType.getName();
      if (!expectedResponseTypeName.endsWith("Impl")) expectedResponseTypeName += "Impl";
      return (W)JsonUtil.Jackson.readValue(result.toString(), Class.forName(expectedResponseTypeName));
    }
    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Unable to create request body object", e);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException("Unable to convert plugin response into expected response type", e);
    }
    catch (ValidationException e) {
      throw new RuntimeException("Attempt to invoke plugin internally with invalid config", e);
    }
  }

  /*****************************************************************
   *** Methods enabling direct calls to the subsetting service's non-tabular endpoints
   ****************************************************************/

  protected long getSubsetCount(String entityId) {
    return getSubsetCount(entityId, _subsetFilters);
  }

  protected long getSubsetCount(String entityId, List<APIFilter> subsetFilters) {
    return EdaSubsettingClient.getSubsetCount(_referenceMetadata, entityId, subsetFilters);
  }

  protected Map<String, Long> getCategoricalCountDistribution(VariableSpec varSpec) {
    return getCategoricalCountDistribution(varSpec, _subsetFilters);
  }

  protected Map<String, Long> getCategoricalCountDistribution(VariableSpec varSpec, List<APIFilter> subsetFilters) {
    VariableDistributionPostResponse response = EdaSubsettingClient.getCategoricalDistribution(_referenceMetadata, varSpec, subsetFilters, ValueSpec.COUNT);
    return response.getHistogram().stream().collect(Collectors.toMap(HistogramBin::getBinLabel, bin -> Long.valueOf(bin.getValue().toString())));
  }

  protected Map<String, Double> getCategoricalProportionDistribution(VariableSpec varSpec) {
    return getCategoricalProportionDistribution(varSpec, _subsetFilters);
  }

  protected Map<String, Double> getCategoricalProportionDistribution(VariableSpec varSpec, List<APIFilter> subsetFilters) {
    VariableDistributionPostResponse response = EdaSubsettingClient.getCategoricalDistribution(_referenceMetadata, varSpec, subsetFilters, ValueSpec.PROPORTION);
    return response.getHistogram().stream().collect(Collectors.toMap(HistogramBin::getBinLabel, bin -> Double.valueOf(bin.getValue().toString())));
  }

  protected Map<String, InputStream> getVocabByRootEntity(DynamicDataSpec dataSpec, List<APIFilter> subsetFilters) {
    return new HashMap<>(1) {{ put(
      getUtil().toColNameOrEmpty(dataSpec),
      EdaSubsettingClient.getVocabByRootEntity(_referenceMetadata, dataSpec, subsetFilters)
    ); }};
  }

  protected Map<String, InputStream> getVocabByRootEntity(DynamicDataSpec dataSpec) {
    return getVocabByRootEntity(dataSpec, _subsetFilters);
  }

  protected Map<String, InputStream> getVocabByRootEntity(List<DynamicDataSpec> dataSpecs) {
    return dataSpecs.parallelStream()
      .map(this::getVocabByRootEntity)
      .map(Map::entrySet)
      .map(Collection::iterator)
      .map(Iterator::next)
      .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  /*****************************************************************
   *** Compute-related methods
   ****************************************************************/

  private static Optional<String> findComputeName(String appName) {
    return Optional.ofNullable(AppsMetadata.APPS.getApps().stream()
      // find this app by name
      .filter(app -> app.getName().equals(appName)).findFirst().orElseThrow()
      // look up compute associated with this app
      .getComputeName());
  }

  private final Supplier<RuntimeException> NO_COMPUTE_EXCEPTION = () ->
    new UnsupportedOperationException("This visualization plugin [" +
      getClass().getSimpleName() + "] is not associated with a compute plugin");

  /**
   * @return the compute name used by this plugin; should only be called if plugin requires a compute
   * @throws NoSuchElementException if not associated with a compute
   */
  protected String getComputeName() {
    return _computeInfo.map(TwoTuple::getFirst).orElseThrow(NO_COMPUTE_EXCEPTION);
  }

  /**
   * @return the compute config for this plugin; should only be called if plugin requires a compute
   * @throws NoSuchElementException if no compute config is present
   */
  protected R getComputeConfig() {
    return _computeInfo.map(TwoTuple::getSecond).orElseThrow(NO_COMPUTE_EXCEPTION);
  }

  protected boolean isComputeResultsAvailable() {
    return EdaComputeClient.isJobResultsAvailable(getComputeName(), createComputeRequestBody());
  }

  protected <Q> Q getComputeResultStats(Class<Q> expectedStatsClass) {
    return EdaComputeClient.getJobStatistics(getComputeName(), createComputeRequestBody(), expectedStatsClass);
  }

  protected void writeComputeStatsResponseToOutput(OutputStream out) {
    try (InputStream statsStream = EdaComputeClient.getJobStatistics(getComputeName(), createComputeRequestBody())) {
      statsStream.transferTo(out);
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to stream stats response from compute service", e);
    }
  }

  protected ComputedVariableMetadata getComputedVariableMetadata() {
    return EdaComputeClient.getJobVariableMetadata(getComputeName(), createComputeRequestBody());
  }

  private ComputeRequestBody createComputeRequestBody() {
    return new ComputeRequestBody(
      _studyId,
      _subsetFilters,
      _derivedVariableSpecs,
      _computeInfo.map(TwoTuple::getSecond).orElseThrow(NO_COMPUTE_EXCEPTION));
  }

  /*****************************************************************
   *** Shared plugin-specific utilities
   ****************************************************************/

  protected void validateBinSpec(BinSpec binSpec, String xVarType) {
    if (xVarType.equals("NUMBER") || xVarType.equals("INTEGER")) {
      if (binSpec.getUnits() != null) {
        LOG.warn("The `units` property of the `BinSpec` class is only used for DATE x-axis variables. It will be ignored.");
      }
    }
    // need an error here if it's a date and we don't have a unit?
  }

  public void validateBinSpec(BinWidthSpec binSpec, String xVarType) {
    validateBinSpec((BinSpec)binSpec, xVarType);
  }

  public static String getBinRangeAsRString(RangeType binRange) {
    if (binRange != null) {
      if (binRange.isNumberRange()) {
        return(getBinRangeAsRString(binRange.getNumberRange()));
      } else {
        return(getBinRangeAsRString(binRange.getDateRange()));
      }
    } else {
      return("binRange <- NULL");
    }
  }

  public static String getBinRangeAsRString(NumberRange binRange) {
    if (binRange != null) {
      return("binRange <- list('min'=" + binRange.getMin().toString() + ", 'max'=" + binRange.getMax().toString() + ")");
    } else {
      return("binRange <- NULL");
    }
  }

  public static String getBinRangeAsRString(DateRange binRange) {
    if (binRange != null) {
      return("binRange <- list('min'='" + binRange.getMin() + "', 'max'='" + binRange.getMax() + "')");
    } else {
      return("binRange <- NULL");
    }
  }

  public static String getViewportAsRString(NumericViewport viewport, String xVarType) {
    if (viewport != null) {
      // think if we just pass the string plot.data will convert it to the claimed type
      if (xVarType.equals("NUMBER") || xVarType.equals("INTEGER")) {
        return("viewport <- list('xMin'=" + viewport.getXMin() + ", 'xMax'=" + viewport.getXMax() + ")");
      } else {
        return("viewport <- list('xMin'=" + singleQuote(viewport.getXMin()) + ", 'xMax'=" + singleQuote(viewport.getXMax()) + ")");
      }
    } else {
      return("viewport <- NULL");
    }
  }

  public static String getViewportAsRString(GeolocationViewport viewport) {
    if (viewport != null) {
      return("viewport <- list('latitude'=list('xMin'= " + viewport.getLatitude().getXMin() +
        ", 'xMax'= " + viewport.getLatitude().getXMax() +
        "), 'longitude'= list('left'= " + viewport.getLongitude().getLeft() +
        ", 'right' = " + viewport.getLongitude().getRight() + "))");
    } else {
      return("viewport <- NULL");
    }
  }

  public String getVariableMetadataRObjectAsString(VariableMapping var) {

    if (var == null) return null;

    StringBuilder variableMetadata = new StringBuilder(
      "veupathUtils::VariableMetadata(" +
        "variableClass=veupathUtils::VariableClass(value='computed')," +
        "variableSpec=veupathUtils::VariableSpec(variableId=" + singleQuote(var.getVariableSpec().getVariableId()) + ",entityId=" + singleQuote(var.getVariableSpec().getEntityId()) + ")," +
        "plotReference=veupathUtils::PlotReference(value=" + singleQuote(var.getPlotReference().getValue()) + ")," +
        "dataType=veupathUtils::DataType(value=" + singleQuote(var.getDataType().toString()) + ")," +
        "dataShape=veupathUtils::DataShape(value=" + singleQuote(var.getDataShape().toString()) + ")," +
        "imputeZero=" + var.getImputeZero().toString().toUpperCase() + "," +
        "isCollection=" + var.getIsCollection().toString().toUpperCase()
    );

    if (var.getDisplayName() != null)
      variableMetadata.append(",displayName=").append(singleQuote(var.getDisplayName()));

    if (var.getDisplayRangeMax() != null && var.getDisplayRangeMin() != null) {
      variableMetadata.append(
        var.getDataType() == APIVariableType.DATE
          ? ",displayRangeMin=" + singleQuote(var.getDisplayRangeMin().toString()) + ",displayRangeMax=" + singleQuote(var.getDisplayRangeMax().toString())
          : ",displayRangeMin=" + var.getDisplayRangeMin().toString() + ",displayRangeMax=" + var.getDisplayRangeMax().toString()
      );
    }

    if (var.getVocabulary() != null)
      variableMetadata.append(",vocabulary=").append(PluginUtil.listToRVector(var.getVocabulary()));

    if (var.getMembers() != null)
      variableMetadata.append(",members=").append(getVariableSpecListRObjectAsString(var.getMembers()));

    return variableMetadata.append(")").toString();
  }

  public String getVoidEvalComputedVariableMetadataList(ComputedVariableMetadata metadata) {
    return
      "computedVariables <- veupathUtils::VariableMetadataList(S4Vectors::SimpleList(" +
        metadata.getVariables().stream()
          .map(this::getVariableMetadataRObjectAsString)
          .filter(Objects::nonNull)
          .collect(Collectors.joining(",")) +
        "))";
  }

  public String getVariableSpecRObjectAsString(VariableSpec var) {
    return getVariableSpecRObjectAsString(var, false);
  }

  public String getVariableSpecRObjectAsString(VariableSpec var, boolean returnEmptyIfNull) {
    if (var == null) return returnEmptyIfNull ? "veupathUtils::VariableSpec()" : null;
    return "veupathUtils::VariableSpec(variableId=" + singleQuote(var.getVariableId()) + ",entityId=" + singleQuote(var.getEntityId()) + ")";
  }

  public String getVariableSpecListRObjectAsString(List<? extends VariableSpec> vars) {
    return getVariableSpecListRObjectAsString(vars, false);
  }

  public String getVariableSpecListRObjectAsString(List<? extends VariableSpec> vars, boolean returnEmptyIfNull) {
    if (vars == null) return returnEmptyIfNull ? "veupathUtils::VariableSpec()" : null;

    StringBuilder sb = new StringBuilder();
    sb.append("veupathUtils::VariableSpecList(S4Vectors::SimpleList(");

    for (VariableSpec var : vars) {
      sb.append(getVariableSpecRObjectAsString(var));
      sb.append(",");
    }

    sb.deleteCharAt(sb.length() - 1);
    sb.append("))");

    return sb.toString();
  }

  public String getVariableMetadataRObjectAsString(CollectionSpec collection, String plotReference) {
    if (collection == null) return null;
    PluginUtil util = getUtil();
    String membersList = getVariableSpecListRObjectAsString(util.getCollectionMembers(collection));
    plotReference = plotReference == null ? "" : "veupathUtils::PlotReference(value=" + singleQuote(plotReference) + "),";
    return
      "veupathUtils::VariableMetadata(" +
        "variableClass=veupathUtils::VariableClass(value='native')," +
        "variableSpec=veupathUtils::VariableSpec(variableId=" + singleQuote(collection.getCollectionId()) + ",entityId=" + singleQuote(collection.getEntityId()) + ")," +
        plotReference +
        "dataType=" + singleQuote(util.getCollectionType(collection)) + "," +
        "dataShape=" + singleQuote(util.getCollectionDataShape(collection)) + "," +
        "imputeZero=" + util.getCollectionImputeZero(collection).toUpperCase() + "," +
        "isCollection=TRUE," +
        "members=" + membersList + ")";
  }

  public String getVariableMetadataRObjectAsString(VariableSpec var, String plotReference) {
    if (var == null) return null;
    PluginUtil util = getUtil();
    String hasStudyDependentVocabulary = util.getHasStudyDependentVocabulary(var) ? "TRUE" : "FALSE";
    plotReference = plotReference == null ? "" : "plotReference=veupathUtils::PlotReference(value=" + singleQuote(plotReference) + "),";
    return
      "veupathUtils::VariableMetadata(" +
        "variableClass=veupathUtils::VariableClass(value='native')," +
        "variableSpec=veupathUtils::VariableSpec(variableId=" + singleQuote(var.getVariableId()) + ",entityId=" + singleQuote(var.getEntityId()) + ")," +
        plotReference +
        "dataType=veupathUtils::DataType(value=" + singleQuote(util.getVariableType(var)) + ")," +
        "dataShape=veupathUtils::DataShape(value=" + singleQuote(util.getVariableDataShape(var)) + ")," +
        "imputeZero=" + util.getVariableImputeZero(var).toUpperCase() + "," +
        "weightingVariableSpec=" + getVariableSpecRObjectAsString(util.getVariableSpecToImputeZeroesFor(var), true) + "," +
        "hasStudyDependentVocabulary=" + hasStudyDependentVocabulary + ")";
  }

  public String getVoidEvalDynamicDataMetadataListWithStudyDependentVocabs(Map<String, DynamicDataSpec> dataSpecs, String outputEntityId) {
    List<DynamicDataSpec> dataSpecsFromMap = new ArrayList<>(dataSpecs.values());
    List<DynamicDataSpec> dataSpecsWithStudyDependentVocabs = getDynamicDataSpecsWithStudyDependentVocabs(outputEntityId, dataSpecsFromMap);
    // for each new data spec with study dependent vocabs, add it to the map
    for (DynamicDataSpec dataSpec : dataSpecsWithStudyDependentVocabs) {
      dataSpecs.put("none" + getDynamicDataSpecId(dataSpec), dataSpec);
    }

    return getVoidEvalDynamicDataMetadataList(dataSpecs);
  }

  public String getVoidEvalDynamicDataMetadataList(Map<String, DynamicDataSpec> dataSpecs) {
    return
      // special case if vars is null or all var values are null
      dataSpecs == null || dataSpecs.values().stream().allMatch(Objects::isNull)
        ? "variables <- veupathUtils::VariableMetadataList()"

        // otherwise build R-friendly var list
        : "variables <- veupathUtils::VariableMetadataList(S4Vectors::SimpleList(" +
          dataSpecs.entrySet().stream()
            .map(entry -> {
              String plotReference = entry.getKey();
              if (plotReference.contains("none") || plotReference.contains("null")) plotReference = null;
              DynamicDataSpec dataSpec = entry.getValue();
              if (dataSpec.isCollectionSpec()) {
                return getVariableMetadataRObjectAsString(dataSpec.getCollectionSpec(), plotReference);
              } else if (dataSpec.isVariableSpec()) {
                return getVariableMetadataRObjectAsString(dataSpec.getVariableSpec(), plotReference);
              } else {
                return null;
              }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.joining(",")) +
          "))";
  }

  public Map<String, DynamicDataSpec> varMapToDynamicDataMap(Map<String, VariableSpec> varSpecs) {
    return varSpecs.entrySet().stream()
      .collect(Collectors.toMap(Entry::getKey, e -> new DynamicDataSpecImpl(e.getValue())));
  }

  public String getVoidEvalVariableMetadataListWithStudyDependentVocabs(Map<String, VariableSpec> varSpecs, String outputEntityId) {
    List<VariableSpec> varSpecsFromMap = new ArrayList<>(varSpecs.values());
    List<VariableSpec> varSpecsWithStudyDependentVocabs = getVariableSpecsWithStudyDependentVocabs(outputEntityId, varSpecsFromMap);
    // for each new varSpec with studyDependentVocabs, add it to varSpecs
    for (VariableSpec varSpec : varSpecsWithStudyDependentVocabs) {
      //ok this is a bit hacky i guess..
      // maybe someday make the varMap like Map<String, List<VariableSpec>>, could help facets too
      varSpecs.put("none" + varSpec.getVariableId(), varSpec);
    }
    return getVoidEvalVariableMetadataList(varSpecs);
  }

  public String getVoidEvalVariableMetadataList(Map<String, VariableSpec> varSpecs) {
    Map<String, DynamicDataSpec> dataSpecs = varMapToDynamicDataMap(varSpecs);

    return getVoidEvalDynamicDataMetadataList(dataSpecs);
  }

  public Map<String, DynamicDataSpec> collectionMapToDynamicDataMap(Map<String, CollectionSpec> collectionSpecs) {
    return collectionSpecs.entrySet().stream()
      .collect(Collectors.toMap(Entry::getKey, e -> new DynamicDataSpecImpl(e.getValue())));
  }

  public String getVoidEvalCollectionMetadataListWithStudyDependentVocabs(Map<String, CollectionSpec> collectionSpecs, String outputEntityId) {
    List<CollectionSpec> collectionSpecsFromMap = new ArrayList<>(collectionSpecs.values());
    List<CollectionSpec> collectionSpecsWithStudyDependentVocabs = getCollectionSpecsWithStudyDependentVocabs(outputEntityId, collectionSpecsFromMap);

    for (CollectionSpec collectionSpec : collectionSpecsWithStudyDependentVocabs) {
      collectionSpecs.put("none" + collectionSpec.getCollectionId(), collectionSpec);
    }

    return getVoidEvalCollectionMetadataList(collectionSpecs);
  }

  public String getVoidEvalCollectionMetadataList(Map<String, CollectionSpec> collectionSpecs) {
    Map<String, DynamicDataSpec> dataSpecs = collectionMapToDynamicDataMap(collectionSpecs);

    return getVoidEvalDynamicDataMetadataList(dataSpecs);
  }

  // TODO remove and replace references once edacommon version is merged
  // deprecated
  public String getRBinListAsString(List<String> labels) {
    StringBuilder rBinList = new StringBuilder("veupathUtils::BinList(S4Vectors::SimpleList(");

    boolean first = true;
    for (String label : labels) {
      String rBin = "veupathUtils::Bin(binLabel='" + label + "'";
      rBin += ")";

      if (first) {
        rBinList.append(rBin);
        first = false;
      } else {
        rBinList.append(",").append(rBin);
      }
    }

    return rBinList + "))";
  }

  public boolean validateImputeZeroesRequest(Map<String, DynamicDataSpec> dataSpecs) {
    // TODO keep adding checks as i think of them

    List<DynamicDataSpec> dataSpecsWithStudyDependentVocabs = dataSpecs.values().stream().filter(this::hasStudyDependentVocabulary).toList();
    List<String> entities = dataSpecsWithStudyDependentVocabs.stream().map(this::getDynamicDataSpecEntityId).toList();

    return entities.isEmpty() || Collections.frequency(entities, entities.getFirst()) == entities.size();
  }

  public String getRStudyVocabsAsString(DynamicDataSpec dataSpec) {
    PluginUtil util = getUtil();

    String studyIdColumnNameAsRString = "rev(" + util.getEntityAncestorsAsRVectorString(getDynamicDataSpecEntityId(dataSpec), _referenceMetadata) + ")[1]";

    String studyVocabInR = "{tmp <- data.table::fread('" + util.toColNameOrEmpty(dataSpec) + "', header=FALSE);" +
      "names(tmp) <- c(" + studyIdColumnNameAsRString + ", '" + util.toColNameOrEmpty(dataSpec) + "');" +
      "tmp}";

    String varSpecAsRString = "veupathUtils::VariableSpec(" +
      "entityId='" + getDynamicDataSpecEntityId(dataSpec)+ "'," +
      "variableId='" + getDynamicDataSpecId(dataSpec) + "')";

    return "veupathUtils::StudySpecificVocabulariesByVariable(" +
      "variableSpec=" + varSpecAsRString + "," +
      "studyIdColumnName=" + studyIdColumnNameAsRString + "," +
      "studyVocab=" + studyVocabInR + ")";
  }

  public String getRStudyVocabsAsString(List<DynamicDataSpec> dataSpecs) {
    if (dataSpecs.isEmpty()) { return "veupathUtils::StudySpecificVocabulariesByVariableList()"; }

    StringBuilder studyVocabListRString = new StringBuilder("veupathUtils::StudySpecificVocabulariesByVariableList(S4Vectors::SimpleList(");
    boolean first = true;

    for (DynamicDataSpec dataSpec : dataSpecs) {
      String studyVocabRString = getRStudyVocabsAsString(dataSpec);
      if (first) {
        studyVocabListRString.append(studyVocabRString);
        first = false;
      } else {
        studyVocabListRString.append(",").append(studyVocabRString);
      }
    }

    return studyVocabListRString + "))";
  }

  public boolean hasStudyDependentVocabulary(DynamicDataSpec dataSpec) {
    PluginUtil util = getUtil();

    if (dataSpec.isCollectionSpec()) {
      return util.getHasStudyDependentVocabulary(dataSpec.getCollectionSpec());
    } else if (dataSpec.isVariableSpec()) {
      return util.getHasStudyDependentVocabulary(dataSpec.getVariableSpec());
    } else {
      return false;
    }
  }

  // TODO consider moving these next two to utils in edacommon
  public String getDynamicDataSpecEntityId(DynamicDataSpec dataSpec) {
    if (dataSpec.isCollectionSpec()) {
      return dataSpec.getCollectionSpec().getEntityId();
    } else if (dataSpec.isVariableSpec()) {
      return dataSpec.getVariableSpec().getEntityId();
    } else {
      return null;
    }
  }

  public String getDynamicDataSpecId(DynamicDataSpec dataSpec) {
    if (dataSpec.isCollectionSpec()) {
      return dataSpec.getCollectionSpec().getCollectionId();
    } else if (dataSpec.isVariableSpec()) {
      return dataSpec.getVariableSpec().getVariableId();
    } else {
      return null;
    }
  }

  public String getRMegastudyAsString(String compressedDataHandle, String outputEntityId, String collectionIdsDataHandle) {
    PluginUtil util = getUtil();

    // find and validate variables w study specific vocabs
    List<DynamicDataSpec> dataSpecsWithStudyDependentVocabs = getDynamicDataSpecsWithStudyDependentVocabs(outputEntityId);
    String studyVocabsAsRString = getRStudyVocabsAsString(dataSpecsWithStudyDependentVocabs);

    String ancestorIdsAsRString = util.getEntityAncestorsAsRVectorString(outputEntityId, _referenceMetadata, true);

    return "veupathUtils::Megastudy(" +
      "data = " + compressedDataHandle + "," +
      "ancestorIdColumns = rev(" + ancestorIdsAsRString + ")," +
      "studySpecificVocabularies = " + studyVocabsAsRString + "," +
      "collectionsDT = data.table::fread('" + collectionIdsDataHandle + "'))";
  }

  // default to ADDITIONAL_STREAM_NAME for the collectionsDT data handle
  public String getRMegastudyAsString(String compressedDataHandle, String outputEntityId) {
    return getRMegastudyAsString(compressedDataHandle, outputEntityId, ADDITIONAL_STREAM_NAME);
  }

  public String getRInputDataWithImputedZeroesAsString(
    String compressedDataHandle, Map<String,
    DynamicDataSpec> dataSpecs,
    String outputEntityId
  ) {
    return getRInputDataWithImputedZeroesAsString(compressedDataHandle, dataSpecs, outputEntityId, "variables");
  }

  public String getRInputDataWithImputedZeroesAsString(
    String compressedDataHandle,
    Map<String, DynamicDataSpec> dataSpecs,
    String outputEntityId,
    String variableMetadataListHandle
  ) {
    boolean validRequest = validateImputeZeroesRequest(dataSpecs);

    if (validRequest) {
      String megastudyData = getRMegastudyAsString(compressedDataHandle, outputEntityId);
      return "veupathUtils::getDTWithImputedZeroes(" + megastudyData + ", " + variableMetadataListHandle + ")";
    }

    return compressedDataHandle;
  }

  public String getRCollectionInputDataWithImputedZeroesAsString(
    String compressedDataHandle,
    Map<String, CollectionSpec> collectionSpecs,
    String outputEntityId
  ) {
    return getRCollectionInputDataWithImputedZeroesAsString(compressedDataHandle, collectionSpecs, outputEntityId, "variables");
  }

  public String getRCollectionInputDataWithImputedZeroesAsString(
    String compressedDataHandle,
    Map<String, CollectionSpec> collectionSpecs,
    String outputEntityId,
    String variableMetadataListHandle
  ) {
    Map<String, DynamicDataSpec> dataSpecs = collectionMapToDynamicDataMap(collectionSpecs);

    return(getRInputDataWithImputedZeroesAsString(compressedDataHandle, dataSpecs, outputEntityId, variableMetadataListHandle));
  }

  public String getRVariableInputDataWithImputedZeroesAsString(
    String compressedDataHandle,
    Map<String, VariableSpec> varSpecs,
    String outputEntityId
  ) {
    return getRVariableInputDataWithImputedZeroesAsString(compressedDataHandle, varSpecs, outputEntityId, "variables");
  }

  public String getRVariableInputDataWithImputedZeroesAsString(
    String compressedDataHandle,
    Map<String, VariableSpec> varSpecs,
    String outputEntityId,
    String variableMetadataListHandle
  ) {
    Map<String, DynamicDataSpec> dataSpecs = varMapToDynamicDataMap(varSpecs);

    return(getRInputDataWithImputedZeroesAsString(compressedDataHandle, dataSpecs, outputEntityId, variableMetadataListHandle));
  }

  // TODO consider adding all that follows to util in edacommon
  public List<VariableSpec> getVariableSpecsWithStudyDependentVocabs(String entityId) {
    List<VariableSpec> varSpecsWithStudyDependentVocabs = new ArrayList<>();
    EntityDef entity = _referenceMetadata.getEntity(entityId).orElseThrow();

    // get reference metadata and find variables w study specific vocabs
    for (VariableDef var : entity.getVariables()) {
      if (var.getHasStudyDependentVocabulary() && var.getEntityId().equals(entityId)) {
        VariableSpecImpl varSpec = new VariableSpecImpl();
        varSpec.setVariableId(var.getVariableId());
        varSpec.setEntityId(var.getEntityId());
        varSpecsWithStudyDependentVocabs.add(varSpec);
      }
    }

    return varSpecsWithStudyDependentVocabs;
  }

  public boolean areSameVariableSpec(VariableSpec varSpec1, VariableSpec varSpec2) {
    if (varSpec1 == null || varSpec2 == null) return false;

    return varSpec1.getVariableId().equals(varSpec2.getVariableId()) &&
      varSpec1.getEntityId().equals(varSpec2.getEntityId());
  }

  public boolean containsVariableSpec(List<VariableSpec> varSpecs, VariableSpec varSpec) {
    return varSpecs.stream().anyMatch(var -> areSameVariableSpec(var, varSpec));
  }

  public boolean areSameDynamicDataSpec(DynamicDataSpec dataSpec1, DynamicDataSpec dataSpec2) {
    if (dataSpec1 == null || dataSpec2 == null) return false;

    return getDynamicDataSpecId(dataSpec1).equals(getDynamicDataSpecId(dataSpec2)) &&
      getDynamicDataSpecEntityId(dataSpec1).equals(getDynamicDataSpecEntityId(dataSpec2));
  }

  public boolean containsDataSpec(List<DynamicDataSpec> dataSpecs, DynamicDataSpec dataSpec) {
    return dataSpecs.stream().anyMatch(data -> areSameDynamicDataSpec(data, dataSpec));
  }

  public boolean areSameCollectionSpec(CollectionSpec colSpec1, CollectionSpec colSpec2) {
    if (colSpec1 == null || colSpec2 == null) return false;

    return colSpec1.getCollectionId().equals(colSpec2.getCollectionId()) &&
      colSpec1.getEntityId().equals(colSpec2.getEntityId());
  }

  public boolean containsCollectionSpec(List<CollectionSpec> colSpecs, CollectionSpec colSpec) {
    return colSpecs.stream().anyMatch(col -> areSameCollectionSpec(col, colSpec));
  }

  public List<VariableSpec> getVariableSpecsWithStudyDependentVocabs(String entityId, List<VariableSpec> varSpecsToIgnore) {
    List<VariableSpec> varSpecsWithStudyDependentVocabs = getVariableSpecsWithStudyDependentVocabs(entityId);
    varSpecsWithStudyDependentVocabs = varSpecsWithStudyDependentVocabs.stream().filter(var -> !containsVariableSpec(varSpecsToIgnore, var)).collect(Collectors.toList());

    return varSpecsWithStudyDependentVocabs;
  }

  public List<CollectionSpec> getCollectionSpecsWithStudyDependentVocabs(String entityId) {
    List<CollectionSpec> collectionSpecsWithStudyDependentVocabs = new ArrayList<>();
    EntityDef entity = _referenceMetadata.getEntity(entityId).orElseThrow();

    // get reference metadata and find collections w study specific vocabs
    for (CollectionDef collection : entity.getCollections()) {
      if (collection.getHasStudyDependentVocabulary() && collection.getEntityId().equals(entityId)) {
        CollectionSpecImpl colSpec = new CollectionSpecImpl();
        colSpec.setCollectionId(collection.getCollectionId());
        colSpec.setEntityId(collection.getEntityId());
        collectionSpecsWithStudyDependentVocabs.add(colSpec);
      }
    }

    return collectionSpecsWithStudyDependentVocabs;
  }

  public List<CollectionSpec> getCollectionSpecsWithStudyDependentVocabs(String entityId, List<CollectionSpec> collectionSpecsToIgnore) {
    return getCollectionSpecsWithStudyDependentVocabs(entityId)
      .stream()
      .filter(col -> !containsCollectionSpec(collectionSpecsToIgnore, col))
      .toList();
  }

  public List<DynamicDataSpec> getDynamicDataSpecsWithStudyDependentVocabs(String entityId, List<DynamicDataSpec> dataSpecsToIgnore) {
    List<DynamicDataSpec> dynamicDataSpecsWithStudyDependentVocabs = new ArrayList<>();

    List<VariableSpec> varSpecsToIgnore = dataSpecsToIgnore.stream()
      .filter(DynamicDataSpec::isVariableSpec)
      .map(DynamicDataSpec::getVariableSpec)
      .toList();
    List<VariableSpec> varSpecsWithStudyDependentVocabs = getVariableSpecsWithStudyDependentVocabs(entityId, varSpecsToIgnore);

    List<CollectionSpec> collectionSpecsToIgnore = dataSpecsToIgnore.stream()
      .filter(DynamicDataSpec::isCollectionSpec)
      .map(DynamicDataSpec::getCollectionSpec)
      .toList();
    List<CollectionSpec> collectionSpecsWithStudyDependentVocabs = getCollectionSpecsWithStudyDependentVocabs(entityId, collectionSpecsToIgnore);

    for (VariableSpec varSpec : varSpecsWithStudyDependentVocabs) {
      DynamicDataSpecImpl dataSpec = new DynamicDataSpecImpl(varSpec);
      dynamicDataSpecsWithStudyDependentVocabs.add(dataSpec);
    }

    for (CollectionSpec colSpec : collectionSpecsWithStudyDependentVocabs) {
      DynamicDataSpecImpl dataSpec = new DynamicDataSpecImpl(colSpec);
      dynamicDataSpecsWithStudyDependentVocabs.add(dataSpec);
    }

    return dynamicDataSpecsWithStudyDependentVocabs;
  }

  public List<DynamicDataSpec> getDynamicDataSpecsWithStudyDependentVocabs(String entityId) {
    return getDynamicDataSpecsWithStudyDependentVocabs(entityId, new ArrayList<>());
  }

  // reverseMatch is for when we want to find entities that are not the specified entity
  // is this interesting enough to go in common?
  public List<VariableSpec> filterVarSpecsByEntityId(List<VariableSpec> varSpecs, String entityId, boolean reverseMatch) {
    if (varSpecs.isEmpty()) {
      return varSpecs;
    }

    if (entityId == null) {
      return varSpecs;
    }

    if (reverseMatch) {
      return varSpecs.stream().filter(varSpec -> !varSpec.getEntityId().equals(entityId)).collect(Collectors.toList());
    }

    return varSpecs.stream().filter(varSpec -> varSpec.getEntityId().equals(entityId)).collect(Collectors.toList());
  }

  // yea i suck, no need to tell me
  public List<VariableSpec> getVarSpecsForStandaloneMapMainStream(String outputEntityId, List<VariableSpec> plotVariableSpecs) {
    PluginUtil util = getUtil();

    List<VariableSpec> varSpecsWithStudyDependentVocabs = getVariableSpecsWithStudyDependentVocabs(outputEntityId);
    VariableSpec weightingVariableSpec = varSpecsWithStudyDependentVocabs.isEmpty()
      ? null
      : util.getVariableSpecToImputeZeroesFor(varSpecsWithStudyDependentVocabs.getFirst());
    boolean needToImputeZeroes = weightingVariableSpec != null && varSpecsWithStudyDependentVocabs.stream()
      .anyMatch(var -> areSameVariableSpec(var, weightingVariableSpec));
    List<VariableSpec> varSpecsForMainRequest = new ArrayList<>(getVariableSpecsWithStudyDependentVocabs(outputEntityId, plotVariableSpecs));

    if (needToImputeZeroes) {
      varSpecsForMainRequest.addAll(filterVarSpecsByEntityId(plotVariableSpecs, outputEntityId, false));
    } else {
      varSpecsForMainRequest.addAll(plotVariableSpecs);
    }

    return varSpecsForMainRequest;
  }
}
