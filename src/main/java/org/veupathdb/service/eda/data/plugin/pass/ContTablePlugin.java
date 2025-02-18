package org.veupathdb.service.eda.data.plugin.pass;

import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.constraint.ConstraintSpec;
import org.veupathdb.service.eda.common.plugin.constraint.DataElementSet;
import org.veupathdb.service.eda.common.plugin.util.PluginUtil;
import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.data.core.AbstractEmptyComputePlugin;
import org.veupathdb.service.eda.generated.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.plugin.util.RServeClient.streamResult;
import static org.veupathdb.service.eda.common.plugin.util.RServeClient.useRConnectionWithRemoteFiles;
import static org.veupathdb.service.eda.data.metadata.AppsMetadata.CLINEPI_PROJECT;

public class ContTablePlugin extends AbstractEmptyComputePlugin<MosaicPostRequest, MosaicSpec> {

  @Override
  public String getDisplayName() {
    return "Mosaic plot, RxC table";
  }

  @Override
  public String getDescription() {
    return "Visualize the frequency distribution and chi-squared test results for two categorical variables";
  }

  @Override
  public List<String> getProjects() {
    return List.of(CLINEPI_PROJECT);
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(MosaicPostRequest.class, MosaicSpec.class);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("yAxisVariable", "xAxisVariable"), List.of("facetVariable"))
      .pattern()
      .element("yAxisVariable")
      .maxValues(8)
      .description("Variable must have 8 or fewer unique values and be from the same branch of the dataset diagram as the X-axis variable.")
      .element("xAxisVariable")
      .maxValues(10)
      .description("Variable must have 10 or fewer unique values and be from the same branch of the dataset diagram as the Y-axis variable.")
      .element("facetVariable")
      .required(false)
      .maxVars(2)
      .maxValues(10)
      .description("Variable(s) must have 10 or fewer unique values and be of the same or a parent entity of the X-axis variable.")
      .done();
  }

  @Override
  protected void validateVisualizationSpec(MosaicSpec pluginSpec) {
    validateInputs(new DataElementSet()
      .entity(pluginSpec.getOutputEntityId())
      .var("xAxisVariable", pluginSpec.getXAxisVariable())
      .var("yAxisVariable", pluginSpec.getYAxisVariable())
      .var("facetVariable", pluginSpec.getFacetVariable()));
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(MosaicSpec pluginSpec) {
    return List.of(
      new StreamSpec(DEFAULT_SINGLE_STREAM_NAME, pluginSpec.getOutputEntityId())
        .addVar(pluginSpec.getXAxisVariable())
        .addVar(pluginSpec.getYAxisVariable())
        .addVars(pluginSpec.getFacetVariable()));
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {
    PluginUtil util = getUtil();
    MosaicSpec spec = getPluginSpec();
    Map<String, VariableSpec> varMap = new HashMap<>();
    varMap.put("xAxis", spec.getXAxisVariable());
    varMap.put("yAxis", spec.getYAxisVariable());
    varMap.put("facet1", util.getVariableSpecFromList(spec.getFacetVariable(), 0));
    varMap.put("facet2", util.getVariableSpecFromList(spec.getFacetVariable(), 1));
    String showMissingness = spec.getShowMissingness() != null ? spec.getShowMissingness().getValue() : "noVariables";
    String deprecatedShowMissingness = showMissingness.equals("FALSE") ? "noVariables" : showMissingness.equals("TRUE") ? "strataVariables" : showMissingness;

    useRConnectionWithRemoteFiles(Resources.RSERVE_URL, dataStreams, connection -> {
      connection.voidEval(util.getVoidEvalFreadCommand(DEFAULT_SINGLE_STREAM_NAME,
        spec.getXAxisVariable(),
        spec.getYAxisVariable(),
        util.getVariableSpecFromList(spec.getFacetVariable(), 0),
        util.getVariableSpecFromList(spec.getFacetVariable(), 1)));
      connection.voidEval(getVoidEvalVariableMetadataList(varMap));
      String cmd = "plot.data::mosaic(" + DEFAULT_SINGLE_STREAM_NAME + ", variables, 'chiSq', NA_character_, NA_character_, NULL, TRUE, TRUE, '" + deprecatedShowMissingness + "')";
      streamResult(connection, cmd, out);
    });
  }
}
