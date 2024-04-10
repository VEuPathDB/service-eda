package org.veupathdb.service.eda.data.plugin.correlation;

import java.util.List;

import org.veupathdb.service.eda.data.metadata.AppsMetadata;
import org.veupathdb.service.eda.data.plugin.correlation.AbstractCorrelationBipartiteNetwork;
import org.veupathdb.service.eda.generated.model.*;

public class CorrelationAssayAssayBipartitenetworkPlugin extends AbstractCorrelationBipartiteNetwork<CorrelationAssayAssayBipartitenetworkPostRequest, CorrelationAssayAssayConfig> {

  @Override
  protected ClassGroup getTypeParameterClasses() {
    return new ClassGroup(CorrelationAssayAssayBipartitenetworkPostRequest.class, CorrelationNetworkSpec.class, CorrelationAssayAssayConfig.class);
  }

  @Override
  public List<String> getProjects() {
    return List.of(AppsMetadata.MICROBIOME_PROJECT);
  }

}