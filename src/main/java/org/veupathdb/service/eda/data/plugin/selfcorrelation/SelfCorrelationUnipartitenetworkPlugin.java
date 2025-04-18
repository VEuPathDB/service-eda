package org.veupathdb.service.eda.data.plugin.selfcorrelation;

import org.veupathdb.service.eda.Resources;
import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.common.plugin.util.RServeClient;
import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.data.core.AbstractPlugin;
import org.veupathdb.service.eda.generated.model.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.veupathdb.service.eda.common.plugin.util.RServeClient.useRConnectionWithRemoteFiles;

public class SelfCorrelationUnipartitenetworkPlugin extends AbstractPlugin<SelfCorrelationUnipartitenetworkPostRequest, CorrelationNetworkSpec, SelfCorrelationConfig> {

  @Override
  public String getDisplayName() {
    return "Network";
  }

  @Override
  public String getDescription() {
    return "Visualize the correlation between variables in a data set";
  }

  @Override
  public List<String> getProjects() {
    return List.of(AppsMetadata.MICROBIOME_PROJECT);
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new ClassGroup(SelfCorrelationUnipartitenetworkPostRequest.class, CorrelationNetworkSpec.class, SelfCorrelationConfig.class);
  }

  @Override
  public boolean computeGeneratesVars() {
    return false;
  }

  @Override
  protected void validateVisualizationSpec(CorrelationNetworkSpec pluginSpec) {
    // nothing to do here
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(CorrelationNetworkSpec pluginSpec) {
    // this plugin only uses the stats result of the compute; no tabular data streams needed
    return Collections.emptyList();
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {
    CorrelationNetworkSpec spec = getPluginSpec();
    String layout = spec.getLayout() != null ? ", layout = '" + spec.getLayout().getValue() + "'" : "";
    ByteArrayOutputStream statsBytes = new ByteArrayOutputStream();
    writeComputeStatsResponseToOutput(statsBytes);
    ByteArrayInputStream statsIn = new ByteArrayInputStream(statsBytes.toByteArray());
    dataStreams.put("statsFile.json", statsIn);

    // some default filtering thresholds
    Number correlationCoefThreshold = getPluginSpec().getCorrelationCoefThreshold() != null ? getPluginSpec().getCorrelationCoefThreshold() : 0.2;
    Number pValueThreshold = getPluginSpec().getSignificanceThreshold() != null ? getPluginSpec().getSignificanceThreshold() : 0.05;

    useRConnectionWithRemoteFiles(Resources.RSERVE_URL, dataStreams, connection -> {
      connection.voidEval("corrResult <- jsonlite::read_json('statsFile.json', simplifyVector = TRUE)");
      connection.voidEval("edgeList <- corrResult$statistics");
      connection.voidEval("names(edgeList) <- c('source', 'target', 'correlationCoef', 'pValue')");
      connection.voidEval("edgeList$pValue <- as.numeric(edgeList$pValue)");
      connection.voidEval("edgeList$correlationCoef <- as.numeric(edgeList$correlationCoef)");
      connection.voidEval("net <- plot.data::CorrelationNetwork(edgeList" +
          ", correlationCoefThreshold = as.numeric(" + correlationCoefThreshold + ")" +
          ", pValueThreshold = as.numeric(" + pValueThreshold + ")" +
          layout + ")");

      String command = "plot.data::writeNetworkJSON(net)";
      RServeClient.streamResult(connection, command, out);
    });
  }
}
