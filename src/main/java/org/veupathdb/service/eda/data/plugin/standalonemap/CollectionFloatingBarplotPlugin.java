package org.veupathdb.service.eda.data.plugin.standalonemap;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
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
import static org.veupathdb.service.eda.common.plugin.util.RServeClient.useRConnectionWithRemoteFiles;
import static org.veupathdb.service.eda.data.metadata.AppsMetadata.VECTORBASE_PROJECT;

public class CollectionFloatingBarplotPlugin extends AbstractEmptyComputePlugin<CollectionFloatingBarplotPostRequest, CollectionFloatingBarplotSpec> {

  @Override
  public String getDisplayName() {
    return "Bar plot";
  }

  @Override
  public String getDescription() {
    return "Visualize the distribution of a group of categorical variables";
  }

  @Override
  public List<String> getProjects() {
    return List.of(VECTORBASE_PROJECT);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("xAxisVariable"), List.of("overlayVariable"))
      .pattern()
        .element("xAxisVariable")
          .maxValues(10)
          .description("Variable Group vocabulary must have 10 or fewer unique values.")
        .element("overlayVariable")
      .done();
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(CollectionFloatingBarplotPostRequest.class, CollectionFloatingBarplotSpec.class);
  }

  @Override
  protected void validateVisualizationSpec(CollectionFloatingBarplotSpec pluginSpec) throws ValidationException {
    List<VariableSpec> collectionMembers = variablesFromCollectionMembers(
      pluginSpec.getOverlayConfig().getCollection(),
      pluginSpec.getOverlayConfig().getSelectedMembers());
    ValidationUtils.validateCollectionMembers(getUtil(),
      pluginSpec.getOverlayConfig().getCollection(),
      collectionMembers);
    if (pluginSpec.getBarMode() == null) {
      throw new ValidationException("Property 'barMode' is required.");
    }
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(CollectionFloatingBarplotSpec pluginSpec) {
    List<VariableSpec> collectionMembers = variablesFromCollectionMembers(
      pluginSpec.getOverlayConfig().getCollection(),
      pluginSpec.getOverlayConfig().getSelectedMembers());
    String outputEntityId = pluginSpec.getOutputEntityId();
    List<VariableSpec> plotVariableSpecs = new ArrayList<>(collectionMembers);

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
    CollectionFloatingBarplotSpec spec = getPluginSpec();
    String outputEntityId = spec.getOutputEntityId();
    String barMode = spec.getBarMode().getValue();
    String overlayValues = getRBinListAsString(spec.getOverlayConfig().getSelectedValues());

    Map<String, CollectionSpec> varMap = new HashMap<>();
    varMap.put("overlay", spec.getOverlayConfig().getCollection());

    List<DynamicDataSpec> dataSpecsWithStudyDependentVocabs = getDynamicDataSpecsWithStudyDependentVocabs(outputEntityId);
    Map<String, InputStream> studyVocabs = getVocabByRootEntity(dataSpecsWithStudyDependentVocabs);
    dataStreams.putAll(studyVocabs);

    useRConnectionWithRemoteFiles(Resources.RSERVE_URL, dataStreams, connection -> {
      connection.voidEval(DEFAULT_SINGLE_STREAM_NAME + " <- data.table::fread('" + DEFAULT_SINGLE_STREAM_NAME + "', na.strings=c(''))");
      String inputData = getRCollectionInputDataWithImputedZeroesAsString(DEFAULT_SINGLE_STREAM_NAME, varMap, outputEntityId, "variables");
      connection.voidEval(getVoidEvalCollectionMetadataListWithStudyDependentVocabs(varMap, outputEntityId));
      String cmd =
        "plot.data::bar(data=" + inputData + ", " +
          "variables=variables, " +
          "value='" + spec.getValueSpec().getValue() + "', " +
          "barmode='" + barMode + "', " +
          "sampleSizes=FALSE, " +
          "completeCases=FALSE, " +
          "overlayValues=" + overlayValues + ", " +
          "evilMode='noVariables')";
      streamResult(connection, cmd, out);
    });
  }
}
