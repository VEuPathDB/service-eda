package org.veupathdb.service.eda.data.plugin.pass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.common.plugin.constraint.DataElementSet;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.common.plugin.util.RFileSetProcessor;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.data.core.AbstractEmptyComputePlugin;
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.plugin.util.RServeClient.streamResult;
import static org.veupathdb.service.eda.common.plugin.util.RServeClient.useRConnectionWithProcessedRemoteFiles;
import static org.veupathdb.service.eda.data.metadata.AppsMetadata.CLINEPI_PROJECT;

public class ScatterplotPlugin extends AbstractEmptyComputePlugin<ScatterplotPostRequest, ScatterplotSpec> {

  private static final Logger LOG = LogManager.getLogger(ScatterplotPlugin.class);

  @Override
  public String getDisplayName() {
    return "Scatter plot";
  }

  @Override
  public String getDescription() {
    return "Visualize the relationship between two continuous variables";
  }

  @Override
  public List<String> getProjects() {
    return List.of(CLINEPI_PROJECT);
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(ScatterplotPostRequest.class, ScatterplotSpec.class);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("yAxisVariable"), List.of("xAxisVariable", "overlayVariable", "facetVariable"))
      .pattern()
        .element("yAxisVariable")
          .types(APIVariableType.NUMBER, APIVariableType.DATE, APIVariableType.INTEGER)
          .description("Variable must be a number or date.")
        .element("xAxisVariable")
          .types(APIVariableType.NUMBER, APIVariableType.DATE, APIVariableType.INTEGER)
          .description("Variable must be a number or date and be of the same or a parent entity as the Y-axis variable.")
        .element("overlayVariable")
          .required(false)
          .maxValues(8)
          .description("Variable must be a number, or have 8 or fewer values, and be of the same or a parent entity as the X-axis variable.")
        .element("facetVariable")
          .required(false)
          .maxVars(2)
          .maxValues(10)
          .description("Variable(s) must have 10 or fewer unique values and be of the same or a parent entity as the Overlay variable.")
      .pattern()
        .element("yAxisVariable")
          .types(APIVariableType.NUMBER, APIVariableType.DATE, APIVariableType.INTEGER)
          .description("Variable must be a number or date.")
        .element("xAxisVariable")
          .types(APIVariableType.NUMBER, APIVariableType.DATE, APIVariableType.INTEGER)
          .description("Variable must be a number or date and be of the same or a parent entity as the Y-axis variable.")
        .element("overlayVariable")
          .types(APIVariableType.NUMBER, APIVariableType.INTEGER)
          .description("Variable must be a number, or have 8 or fewer values, and be of the same or a parent entity as the X-axis variable.")
        .element("facetVariable")
          .required(false)
          .maxVars(2)
          .maxValues(10)
          .description("Variable(s) must have 10 or fewer unique values and be of the same or a parent entity as the Overlay variable.")
      .done();
  }

  @Override
  protected void validateVisualizationSpec(ScatterplotSpec pluginSpec) throws ValidationException {
    validateInputs(new DataElementSet()
      .entity(pluginSpec.getOutputEntityId())
      .var("xAxisVariable", pluginSpec.getXAxisVariable())
      .var("yAxisVariable", pluginSpec.getYAxisVariable())
      .var("overlayVariable", pluginSpec.getOverlayVariable())
      .var("facetVariable", pluginSpec.getFacetVariable()));
    if (pluginSpec.getMaxAllowedDataPoints() != null && pluginSpec.getMaxAllowedDataPoints() <= 0) {
      throw new ValidationException("maxAllowedDataPoints must be a positive integer");
    }
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(ScatterplotSpec pluginSpec) {
    return List.of(
      new StreamSpec(DEFAULT_SINGLE_STREAM_NAME, pluginSpec.getOutputEntityId())
        .addVar(pluginSpec.getXAxisVariable())
        .addVar(pluginSpec.getYAxisVariable())
        .addVar(pluginSpec.getOverlayVariable())
        .addVars(pluginSpec.getFacetVariable()));
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {
    PluginUtil util = getUtil();
    ScatterplotSpec spec = getPluginSpec();
    Map<String, VariableSpec> varMap = new HashMap<>();
    varMap.put("xAxis", spec.getXAxisVariable());
    varMap.put("yAxis", spec.getYAxisVariable());
    varMap.put("overlay", spec.getOverlayVariable());
    varMap.put("facet1", util.getVariableSpecFromList(spec.getFacetVariable(), 0));
    varMap.put("facet2", util.getVariableSpecFromList(spec.getFacetVariable(), 1));
    String valueSpec = spec.getValueSpec().getValue();
    String showMissingness = spec.getShowMissingness() != null ? spec.getShowMissingness().getValue() : "noVariables";
    String deprecatedShowMissingness = showMissingness.equals("FALSE") ? "noVariables" : showMissingness.equals("TRUE") ? "strataVariables" : showMissingness;
    String yVarType = util.getVariableType(spec.getYAxisVariable());
    String correlationMethod = spec.getCorrelationMethod() != null ? spec.getCorrelationMethod().getValue() : "none";
    String recordIdColumnName = util.toColNameOrEmpty(util.getEntityIdVarSpec(spec.getOutputEntityId()));
    String returnPointIds = spec.getReturnPointIds() != null ? String.valueOf(spec.getReturnPointIds()).toUpperCase() : "FALSE";

    if (yVarType.equals("DATE") && !valueSpec.equals("raw")) {
      LOG.error("Cannot calculate trend lines for y-axis date variables. The `valueSpec` property must be set to `raw`.");
    }

    List<String> nonStrataVarColNames = new ArrayList<>();
    nonStrataVarColNames.add(util.toColNameOrEmpty(spec.getXAxisVariable()));
    nonStrataVarColNames.add(util.toColNameOrEmpty(spec.getYAxisVariable()));

    RFileSetProcessor filesProcessor = new RFileSetProcessor(dataStreams)
      .add(DEFAULT_SINGLE_STREAM_NAME,
        spec.getMaxAllowedDataPoints(),
        deprecatedShowMissingness,
        nonStrataVarColNames,
        (name, conn) ->
        conn.voidEval(util.getVoidEvalFreadCommand(name,
          util.getEntityIdVarSpec(spec.getOutputEntityId()),
          spec.getXAxisVariable(),
          spec.getYAxisVariable(),
          spec.getOverlayVariable(),
          util.getVariableSpecFromList(spec.getFacetVariable(), 0),
          util.getVariableSpecFromList(spec.getFacetVariable(), 1)))
      );

    useRConnectionWithProcessedRemoteFiles(Resources.RSERVE_URL, filesProcessor, connection -> {
      connection.voidEval(getVoidEvalVariableMetadataList(varMap));
      String cmd =
          "plot.data::scattergl(" + 
            DEFAULT_SINGLE_STREAM_NAME +
            ", variables" +
            ", value= '" + valueSpec +
            "', overlayValues = NULL" + 
            ", correlationMethod = '" + correlationMethod + 
            "', sampleSizes=TRUE, completeCases=TRUE" +
            ", evilMode='" + deprecatedShowMissingness + 
            "', idColumn = '" + recordIdColumnName + 
            "', returnPointIds = " + returnPointIds + ")";

      streamResult(connection, cmd, out);
    });
  }
}
