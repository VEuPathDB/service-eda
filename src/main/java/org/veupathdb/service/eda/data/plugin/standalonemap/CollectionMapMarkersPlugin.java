package org.veupathdb.service.eda.data.plugin.standalonemap;

import org.gusdb.fgputil.DelimitedDataParser;
import org.gusdb.fgputil.json.JsonUtil;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.data.core.AbstractEmptyComputePlugin;
import org.veupathdb.service.eda.data.plugin.standalonemap.aggregator.AveragesWithConfidence;
import org.veupathdb.service.eda.data.plugin.standalonemap.aggregator.CollectionAveragesWithConfidenceAggregator;
import org.veupathdb.service.eda.data.plugin.standalonemap.aggregator.MarkerAggregator;
import org.veupathdb.service.eda.data.plugin.standalonemap.conversion.ApiConverter;
import org.veupathdb.service.eda.data.plugin.standalonemap.markers.GeolocationViewport;
import org.veupathdb.service.eda.data.plugin.standalonemap.markers.MapMarkerRowProcessor;
import org.veupathdb.service.eda.data.plugin.standalonemap.markers.MarkerData;
import org.veupathdb.service.eda.data.plugin.standalonemap.markers.QuantitativeAggregateConfiguration;
import org.veupathdb.service.eda.data.utils.ValidationUtils;
import org.veupathdb.service.eda.generated.model.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.gusdb.fgputil.FormatUtil.TAB;
import static org.veupathdb.service.eda.data.metadata.AppsMetadata.VECTORBASE_PROJECT;

public class CollectionMapMarkersPlugin extends AbstractEmptyComputePlugin<StandaloneCollectionMapMarkerPostRequest, StandaloneCollectionMapMarkerSpec> {
  private QuantitativeAggregateConfiguration _aggregateConfig;

  @Override
  public List<String> getProjects() {
    return List.of(VECTORBASE_PROJECT);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("geoAggregateVariable", "latitudeVariable", "longitudeVariable"))
      .pattern()
        .element("geoAggregateVariable")
          .types(APIVariableType.STRING)
        .element("latitudeVariable")
          .types(APIVariableType.NUMBER)
        .element("longitudeVariable")
          .types(APIVariableType.NUMBER)
      .done();
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(StandaloneCollectionMapMarkerPostRequest.class, StandaloneCollectionMapMarkerSpec.class);
  }

  @Override
  protected void validateVisualizationSpec(StandaloneCollectionMapMarkerSpec pluginSpec) throws ValidationException {
    if (pluginSpec.getCollectionOverlay() == null) {
      throw new ValidationException("Collection information must be specified.");
    }
    CollectionSpec collection = pluginSpec.getCollectionOverlay().getCollection();
    List<VariableSpec> collectionMembers = PluginUtil.variablesFromCollectionMembers(collection, pluginSpec.getCollectionOverlay().getSelectedMembers());
    ValidationUtils.validateCollectionMembers(getUtil(), collection, collectionMembers);
    if (pluginSpec.getAggregatorConfig() != null) {
      try {
        _aggregateConfig = new QuantitativeAggregateConfiguration(pluginSpec.getAggregatorConfig(),
          getUtil().getCollectionDataShape(collection),
          getUtil().getCollectionType(collection),
          () -> getUtil().getCollectionVocabulary(collection));
      } catch (IllegalArgumentException e) {
        throw new ValidationException(e.getMessage());
      }
    }
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(StandaloneCollectionMapMarkerSpec pluginSpec) {
    StreamSpec streamSpec = new StreamSpec(DEFAULT_SINGLE_STREAM_NAME, pluginSpec.getOutputEntityId());
    List<VariableSpec> collectionMembers = PluginUtil.variablesFromCollectionMembers(pluginSpec.getCollectionOverlay().getCollection(),
      pluginSpec.getCollectionOverlay().getSelectedMembers());
    streamSpec.addVars(collectionMembers)
      .addVar(pluginSpec.getGeoAggregateVariable())
      .addVar(pluginSpec.getLatitudeVariable())
      .addVar(pluginSpec.getLongitudeVariable());
    return List.of(streamSpec);
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) throws IOException {
    InputStreamReader isReader = new InputStreamReader(new BufferedInputStream(dataStreams.get(DEFAULT_SINGLE_STREAM_NAME)));
    BufferedReader reader = new BufferedReader(isReader);
    DelimitedDataParser parser = new DelimitedDataParser(reader.readLine(), TAB, true);

    StandaloneCollectionMapMarkerSpec spec = getPluginSpec();
    Function<String, Integer> indexOf = var ->
      parser.indexOfColumn(var).orElseThrow(() -> new RuntimeException("Looking for variable " + var + " but found columns " + parser.getColumnNames()));

    Function<Integer, String> indexToVarId = index -> parser.getColumnNames().get(index);
    List<VariableSpec> collectionMembers = PluginUtil.variablesFromCollectionMembers(spec.getCollectionOverlay().getCollection(),
      spec.getCollectionOverlay().getSelectedMembers());

    List<String> memberVarColNames = collectionMembers.stream()
      .map(getUtil()::toColNameOrEmpty)
      .toList();

    // For each marker, aggregate all data into a Map of collection member ID to stats containing averages and confidence intervals
    final Supplier<MarkerAggregator<Map<String, AveragesWithConfidence>>> aggSupplier = () -> new CollectionAveragesWithConfidenceAggregator(indexToVarId,
      indexOf, memberVarColNames, _aggregateConfig);

    // Establish column header indexes
    int geoVarIndex = indexOf.apply(getUtil().toColNameOrEmpty(spec.getGeoAggregateVariable()));
    int latIndex = indexOf.apply(getUtil().toColNameOrEmpty(spec.getLatitudeVariable()));
    int lonIndex = indexOf.apply(getUtil().toColNameOrEmpty(spec.getLongitudeVariable()));

    GeolocationViewport viewport = GeolocationViewport.fromApiViewport(spec.getViewport());
    MapMarkerRowProcessor<Map<String, AveragesWithConfidence>> processor = new MapMarkerRowProcessor<>(geoVarIndex, latIndex, lonIndex);
    Map<String, MarkerData<Map<String, AveragesWithConfidence>>> markerDataById = processor.process(reader, parser, viewport, aggSupplier);

    // Construct response, serialize and flush output
    final StandaloneCollectionMapMarkerPostResponse response = new StandaloneCollectionMapMarkerPostResponseImpl();
    response.setMarkers(markerDataById.entrySet().stream()
      .filter(marker -> marker.getValue().getCount() != 0)
      .map(entry -> {
        final CollectionMapMarkerElement ele = new CollectionMapMarkerElementImpl();
        ApiConverter.populateBaseMarkerData(entry.getKey(), ele, entry.getValue());
        ele.setOverlayValues(entry.getValue().getMarkerAggregator().finish().entrySet().stream()
          .map(markerAggregate -> translateToOutput(markerAggregate.getValue(), markerAggregate.getKey()))
          .toList());
        return ele;
    }).toList());

    JsonUtil.Jackson.writeValue(out, response);
    out.flush();
  }

  private CollectionMemberAggregate translateToOutput(AveragesWithConfidence averagesWithConfidence, String variableDotNotation) {
    final CollectionMemberAggregate collectionMemberResult = new CollectionMemberAggregateImpl();
    collectionMemberResult.setValue(averagesWithConfidence.getAverage());
    collectionMemberResult.setN(averagesWithConfidence.getN());
    collectionMemberResult.setVariableId(variableDotNotation.split("[.]")[1]);
    final NumberRange range = new NumberRangeImpl();
    range.setMin(averagesWithConfidence.getIntervalLowerBound());
    range.setMax(averagesWithConfidence.getIntervalUpperBound());
    collectionMemberResult.setConfidenceInterval(range);
    return collectionMemberResult;
  }
}
