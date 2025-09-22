package org.veupathdb.service.eda.data.plugin.dimensionalityreduction;

import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.common.plugin.constraint.DataElementSet;
import org.veupathdb.service.eda.common.plugin.util.RServeClient;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.data.core.AbstractPlugin;
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.plugin.util.RServeClient.useRConnectionWithRemoteFiles;

public class DimensionalityReductionScatterplotPlugin extends AbstractPlugin<DimensionalityReductionScatterplotPostRequest, DimensionalityReductionScatterplotSpec, DimensionalityReductionComputeConfig> {

  @Override
  public String getDisplayName() {
    return "Dimensional reduction scatter plot";
  }

  @Override
  public String getDescription() {
    return "Visualize a 2-dimensional projection of samples based on their beta diversitiy";
  }

  @Override
  public List<String> getProjects() {
    return List.of(AppsMetadata.MICROBIOME_PROJECT);
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new ClassGroup(DimensionalityReductionScatterplotPostRequest.class, DimensionalityReductionScatterplotSpec.class, DimensionalityReductionComputeConfig.class);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("yAxisVariable", "xAxisVariable"), List.of("overlayVariable"))
      .pattern()
        .element("overlayVariable")
          .required(false)
          .maxValues(8)
          .description("Variable must be a number, or have 8 or fewer values, and be of the same or a parent entity as the X-axis variable.")
      .pattern()
        .element("overlayVariable")
          .required(false)
          .types(APIVariableType.NUMBER, APIVariableType.INTEGER)
          .description("Variable must be a number, or have 8 or fewer values, and be of the same or a parent entity as the X-axis variable.")
      .done();
  }

  @Override
  protected void validateVisualizationSpec(DimensionalityReductionScatterplotSpec pluginSpec) {
    validateInputs(new DataElementSet()
      .entity(pluginSpec.getOutputEntityId())
      .var("overlayVariable", pluginSpec.getOverlayVariable()));
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(DimensionalityReductionScatterplotSpec pluginSpec) {
    return List.of(
      new StreamSpec(DEFAULT_SINGLE_STREAM_NAME, pluginSpec.getOutputEntityId())
        .addVar(pluginSpec.getOverlayVariable())
        .setIncludeComputedVars(true)
    );
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {

    DimensionalityReductionScatterplotSpec spec = getPluginSpec();
    Map<String, VariableSpec> varMap = new HashMap<>();
    varMap.put("overlay", spec.getOverlayVariable());
    String valueSpec = "raw";
    String showMissingness = spec.getShowMissingness() != null
      ? spec.getShowMissingness().getValue()
      : "noVariables";
    String deprecatedShowMissingness = showMissingness.equals("FALSE")
      ? "noVariables"
      : showMissingness.equals("TRUE")
        ? "strataVariables"
        : showMissingness;

    ComputedVariableMetadata computedMetadata = getComputedVariableMetadata();

    VariableSpec xComputedVarSpec = computedMetadata.getVariables().stream()
        .filter(var -> var.getVariableSpec().getVariableId().equals(spec.getXAxisSelection()))
        .findFirst().orElseThrow().getVariableSpec();
    VariableSpec yComputedVarSpec = computedMetadata.getVariables().stream()
    .filter(var -> var.getVariableSpec().getVariableId().equals(spec.getYAxisSelection()))
    .findFirst().orElseThrow().getVariableSpec();


    useRConnectionWithRemoteFiles(Resources.RSERVE_URL, dataStreams, connection -> {
      connection.voidEval(getUtil().getVoidEvalFreadCommand(DEFAULT_SINGLE_STREAM_NAME,
          xComputedVarSpec,
          yComputedVarSpec,
          spec.getOverlayVariable()));


      // Creates 'variables' R variable
      connection.voidEval(getVoidEvalVariableMetadataList(varMap));
      // Creates 'computedVariables' R variable
      connection.voidEval(getVoidEvalComputedVariableMetadataList(computedMetadata));
      
      connection.voidEval("variables <- veupathUtils::merge(variables, computedVariables)");

      // Set axis variables
      connection.voidEval("xIndex <- Position(function(x) x@variableSpec@variableId == '" + spec.getXAxisSelection() + "', variables)");
      connection.voidEval("variables[[xIndex]]@plotReference <- veupathUtils::PlotReference(value = 'xAxis')");
      connection.voidEval("yIndex <- Position(function(x) x@variableSpec@variableId == '" + spec.getYAxisSelection() + "', variables)");
      connection.voidEval("variables[[yIndex]]@plotReference <- veupathUtils::PlotReference(value = 'yAxis')");


      String command = "plot.data::scattergl(" + DEFAULT_SINGLE_STREAM_NAME + ", variables, '" +
        valueSpec +
        "', overlayValues=NULL, correlationMethod = 'none', sampleSizes=TRUE, completeCases=TRUE, '" +
        deprecatedShowMissingness + "')";
      RServeClient.streamResult(connection, command, out);
    });
  }
}
