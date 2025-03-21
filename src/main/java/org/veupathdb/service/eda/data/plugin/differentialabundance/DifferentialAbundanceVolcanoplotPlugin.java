package org.veupathdb.service.eda.data.plugin.differentialabundance;

import org.veupathdb.service.eda.common.client.spec.StreamSpec;
import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.data.core.AbstractPlugin;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceComputeConfig;
import org.veupathdb.service.eda.generated.model.DifferentialAbundanceVolcanoplotPostRequest;
import org.veupathdb.service.eda.generated.model.EmptyDataPluginSpec;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DifferentialAbundanceVolcanoplotPlugin extends AbstractPlugin<DifferentialAbundanceVolcanoplotPostRequest, EmptyDataPluginSpec, DifferentialAbundanceComputeConfig> {

  @Override
  public String getDisplayName() {
    return "Volcano plot";
  }

  @Override
  public String getDescription() {
    return "Display effect size vs. significance for a differential abundance analysis.";
  }

  @Override
  public List<String> getProjects() {
    return List.of(AppsMetadata.MICROBIOME_PROJECT);
  }

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new ClassGroup(DifferentialAbundanceVolcanoplotPostRequest.class, EmptyDataPluginSpec.class, DifferentialAbundanceComputeConfig.class);
  }

  @Override
  protected boolean computeGeneratesVars() {
    return false;
  }

  @Override
  protected void validateVisualizationSpec(EmptyDataPluginSpec pluginSpec) {
    // nothing to do here
  }

  @Override
  protected List<StreamSpec> getRequestedStreams(EmptyDataPluginSpec pluginSpec) {
    // this plugin only uses the stats result of the compute; no tabular data streams needed
    return Collections.emptyList();
  }

  @Override
  protected void writeResults(OutputStream out, Map<String, InputStream> dataStreams) {
    writeComputeStatsResponseToOutput(out);
  }
}
