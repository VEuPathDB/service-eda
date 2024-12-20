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
import static org.veupathdb.service.eda.data.metadata.AppsMetadata.MICROBIOME_PROJECT;

public class DensityplotPlugin extends AbstractEmptyComputePlugin<DensityplotPostRequest, DensityplotSpec> {

  @Override
  public String getDisplayName() {
    return "Density plot";
  }

  @Override
  public String getDescription() {
    return "Visualize the smoothed distribution (using a kernel density estimate) of a continuous variable";
  }

  @Override
  public List<String> getProjects() {
    return List.of(CLINEPI_PROJECT, MICROBIOME_PROJECT);
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new EmptyComputeClassGroup(DensityplotPostRequest.class, DensityplotSpec.class);
  }

  @Override
  public ConstraintSpec getConstraintSpec() {
    return new ConstraintSpec()
      .dependencyOrder(List.of("xAxisVariable"), List.of("overlayVariable", "facetVariable"))
      .pattern()
      .element("xAxisVariable")
      .types(APIVariableType.NUMBER, APIVariableType.INTEGER)
      .description("Variable must be a number.")
      .element("overlayVariable")
      .required(false)
      .maxValues(8)
      .description("Variable must have 8 or fewer unique values and be of the same or a parent entity of the X-axis variable.")
      .element("facetVariable")
      .required(false)
      .maxVars(2)
      .maxValues(10)
      .description("Variable(s) must have 10 or fewer unique values and be of the same or a parent entity of the Overlay variable.")
      .done();
  }

  @Override
  protected void validateVisualizationSpec(DensityplotSpec pluginSpec) {
    validateInputs(new DataElementSet()
      .entity(pluginSpec.getOutputEntityId())
      .var("xAxisVariable", pluginSpec.getXAxisVariable())
      .var("overlayVariable", pluginSpec.getOverlayVariable())
      .var("facetVariable", pluginSpec.getFacetVariable()));
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(DensityplotSpec pluginSpec) {
    return List.of(
      new StreamSpec(DEFAULT_SINGLE_STREAM_NAME, pluginSpec.getOutputEntityId())
        .addVar(pluginSpec.getXAxisVariable())
        .addVar(pluginSpec.getOverlayVariable())
        .addVars(pluginSpec.getFacetVariable()));
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {
    PluginUtil util = getUtil();
    DensityplotSpec spec = getPluginSpec();
    Map<String, VariableSpec> varMap = new HashMap<>();
    varMap.put("xAxis", spec.getXAxisVariable());
    varMap.put("overlay", spec.getOverlayVariable());
    varMap.put("facet1", util.getVariableSpecFromList(spec.getFacetVariable(), 0));
    varMap.put("facet2", util.getVariableSpecFromList(spec.getFacetVariable(), 1));
    String showMissingness = spec.getShowMissingness() != null ? spec.getShowMissingness().getValue() : "noVariables";
    String deprecatedShowMissingness = showMissingness.equals("FALSE") ? "noVariables" : showMissingness.equals("TRUE") ? "strataVariables" : showMissingness;

    useRConnectionWithRemoteFiles(Resources.RSERVE_URL, dataStreams, connection -> {
      connection.voidEval(util.getVoidEvalFreadCommand(DEFAULT_SINGLE_STREAM_NAME,
        spec.getXAxisVariable(),
        spec.getOverlayVariable(),
        util.getVariableSpecFromList(spec.getFacetVariable(), 0),
        util.getVariableSpecFromList(spec.getFacetVariable(), 1)));
      connection.voidEval(getVoidEvalVariableMetadataList(varMap));
      String cmd = "plot.data::scattergl(" + DEFAULT_SINGLE_STREAM_NAME + ", variables, 'density','" + deprecatedShowMissingness + "')";
      streamResult(connection, cmd, out);
    });
  }
}
