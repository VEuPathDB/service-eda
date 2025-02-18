package org.veupathdb.service.eda.data.plugin.standalonemap;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.common.plugin.constraint.DataElementSet;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.common.plugin.util.RFileSetProcessor;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.data.core.AbstractEmptyComputePlugin;
import org.veupathdb.service.eda.data.utils.ValidationUtils;
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.plugin.util.PluginUtil.variablesFromCollectionMembers;
import static org.veupathdb.service.eda.common.plugin.util.RServeClient.streamResult;
import static org.veupathdb.service.eda.common.plugin.util.RServeClient.useRConnectionWithProcessedRemoteFiles;
import static org.veupathdb.service.eda.data.metadata.AppsMetadata.VECTORBASE_PROJECT;

public class CollectionFloatingBoxplotPlugin extends AbstractEmptyComputePlugin<CollectionFloatingBoxplotPostRequest, CollectionFloatingBoxplotSpec> {

  @Override
  public String getDisplayName() {
    return "Box plot";
  }

  @Override
  public String getDescription() {
    return "Visualize summary values for a continuous Variable Group";
  }

  @Override
  public List<String> getProjects() {
    return List.of(VECTORBASE_PROJECT);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("yAxisVariable"), List.of("xAxisVariable", "overlayVariable"))
      .pattern()
        .element("yAxisVariable")
          .types(APIVariableType.NUMBER, APIVariableType.INTEGER)
          .description("Variable Group must be a number.")
        .element("xAxisVariable")
          .maxValues(10)
          .description("Variable must have 10 or fewer unique values and be the same or a child entity as the variable the map markers are painted with.")
        .element("overlayVariable")
      .done();
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(CollectionFloatingBoxplotPostRequest.class, CollectionFloatingBoxplotSpec.class);
  }

  @Override
  protected void validateVisualizationSpec(CollectionFloatingBoxplotSpec pluginSpec) throws ValidationException {
    List<VariableSpec> collectionMembers = variablesFromCollectionMembers(
        pluginSpec.getOverlayConfig().getCollection(),
        pluginSpec.getOverlayConfig().getSelectedMembers());
    validateInputs(new DataElementSet()
      .entity(pluginSpec.getOutputEntityId())
      .var("xAxisVariable", pluginSpec.getXAxisVariable()));
    ValidationUtils.validateCollectionMembers(getUtil(),
        pluginSpec.getOverlayConfig().getCollection(),
        collectionMembers);
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(CollectionFloatingBoxplotSpec pluginSpec) {
    List<VariableSpec> collectionMembers = variablesFromCollectionMembers(
      pluginSpec.getOverlayConfig().getCollection(),
      pluginSpec.getOverlayConfig().getSelectedMembers());
    String outputEntityId = pluginSpec.getOutputEntityId();
    List<VariableSpec> plotVariableSpecs = new ArrayList<>();
    plotVariableSpecs.add(pluginSpec.getXAxisVariable());
    plotVariableSpecs.addAll(collectionMembers);

    List<VariableSpec> varSpecsForMainRequest = getVarSpecsForStandaloneMapMainStream(outputEntityId, plotVariableSpecs);

    return List.of(
      new StreamSpec(DEFAULT_SINGLE_STREAM_NAME, outputEntityId)
        .addVars(varSpecsForMainRequest),
      new StreamSpec(ADDITIONAL_STREAM_NAME, outputEntityId)
        .addVars(filterVarSpecsByEntityId(plotVariableSpecs, outputEntityId, true))
    );
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {
    PluginUtil util = getUtil();
    CollectionFloatingBoxplotSpec spec = getPluginSpec();
    String outputEntityId = spec.getOutputEntityId();
    List<VariableSpec> inputVarSpecs = variablesFromCollectionMembers(
      spec.getOverlayConfig().getCollection(),
      spec.getOverlayConfig().getSelectedMembers());

    inputVarSpecs.add(spec.getXAxisVariable());
    CollectionSpec overlayVariable = spec.getOverlayConfig().getCollection();
    Map<String, DynamicDataSpec> varMap = new HashMap<>();
    varMap.put("xAxis", new DynamicDataSpecImpl(spec.getXAxisVariable()));
    varMap.put("overlay", new DynamicDataSpecImpl(overlayVariable));

    List<String> nonStrataVarColNames = new ArrayList<>();
    nonStrataVarColNames.add(util.toColNameOrEmpty(spec.getXAxisVariable()));
    // ideally wed find another way to account for the yaxis given its a collection but that seems hard and idk if were even using this feature
    //nonStrataVarColNames.add(util.toColNameOrEmpty(spec.getYAxisVariable()));

    // TODO we have this and the inpute fxn below. how do they relate?
    RFileSetProcessor filesProcessor = new RFileSetProcessor(dataStreams)
      .add(DEFAULT_SINGLE_STREAM_NAME,
        spec.getMaxAllowedDataPoints(),
        "noVariables",
        nonStrataVarColNames,
        (name, conn) ->
        conn.voidEval(name + " <- data.table::fread('" + name + "', na.strings=c(''))")
      );

    List<DynamicDataSpec> dataSpecsWithStudyDependentVocabs = getDynamicDataSpecsWithStudyDependentVocabs(outputEntityId);
    Map<String, InputStream> studyVocabs = getVocabByRootEntity(dataSpecsWithStudyDependentVocabs);
    dataStreams.putAll(studyVocabs);

    useRConnectionWithProcessedRemoteFiles(Resources.RSERVE_URL, filesProcessor, connection -> {
      String inputData = getRInputDataWithImputedZeroesAsString(DEFAULT_SINGLE_STREAM_NAME, varMap, outputEntityId, "variables");
      connection.voidEval(getVoidEvalDynamicDataMetadataListWithStudyDependentVocabs(varMap, outputEntityId));
      String cmd =
        "plot.data::box(data=" + inputData + ", " +
          "variables=variables, " +
          "points='outliers', " +
          "mean=TRUE, " +
          "computeStats=FALSE, " +
          "sampleSizes=FALSE, " +
          "completeCases=FALSE, " +
          "overlayValues=NULL, " +
          "evilMode='noVariables')";
      streamResult(connection, cmd, out);
    });
  }
}
